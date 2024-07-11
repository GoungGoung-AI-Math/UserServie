#!/bin/bash

# Define variables
EC2_USER=ec2-user
EC2_IP=13.209.22.119
PEM_PATH=~/Desktop/aikey.pem
LOCAL_PATH=$(dirname "$0")
REMOTE_PATH=/home/$EC2_USER/userapp
KEYSTORE_LOCAL_PATH="/Users/makisekurisu/key1.p12"
KEYSTORE_REMOTE_PATH="$REMOTE_PATH/key1.p12"
TOKEN_PATH="/home/ec2-user/userapp/vault_token.txt"

# Clean and build the project
echo "Building the project..."
cd $LOCAL_PATH
./gradlew clean build

# Check if build was successful
if [ $? -ne 0 ]; then
  echo "Build failed. Exiting..."
  exit 1
fi

# Create the remote directory if it doesn't exist
ssh -i $PEM_PATH $EC2_USER@$EC2_IP "mkdir -p $REMOTE_PATH"

# Copy files to EC2
echo "Copying files to EC2..."
scp -i $PEM_PATH -r $LOCAL_PATH/docker-compose.yml $LOCAL_PATH/Dockerfile $LOCAL_PATH/build/libs/Math.AI-0.0.1-SNAPSHOT.jar $KEYSTORE_LOCAL_PATH $EC2_USER@$EC2_IP:$REMOTE_PATH

# SSH into EC2 and run Docker Compose
echo "Connecting to EC2 and starting Docker Compose..."
ssh -t -i $PEM_PATH $EC2_USER@$EC2_IP << EOF
  # Read the Vault token from the file
  VAULT_TOKEN=\$(cat $TOKEN_PATH)
  export VAULT_ADDR='https://test.udongrang.com:8200'
  export VAULT_TOKEN

  echo "Using Vault token: \$VAULT_TOKEN"

  # Fetch secrets using Vault CLI
  echo "Fetching database secrets using Vault CLI..."
  DB_SECRETS=\$(vault kv get -format=json kv/db-creds)
  DB_USERNAME=\$(echo \$DB_SECRETS | jq -r '.data.data.username_user_db')
  DB_PASSWORD=\$(echo \$DB_SECRETS | jq -r '.data.data.password_user_db')
  echo "Database credentials - Username: \$DB_USERNAME, Password: \$DB_PASSWORD"

  # Use existing SSL certificates
  echo "Using existing SSL certificates..."

  # Modify docker-compose.yml directly to set environment variables
  sed -i "s/SPRING_DATASOURCE_USERNAME: springuser/SPRING_DATASOURCE_USERNAME: \$DB_USERNAME/" $REMOTE_PATH/docker-compose.yml
  sed -i "s/SPRING_DATASOURCE_PASSWORD: password/SPRING_DATASOURCE_PASSWORD: \$DB_PASSWORD/" $REMOTE_PATH/docker-compose.yml

  # Add SSL environment variables to docker-compose.yml if not already present
  if ! grep -q "SERVER_SSL_KEY_STORE" $REMOTE_PATH/docker-compose.yml; then
    sed -i "/SPRING_DATASOURCE_PASSWORD: \$DB_PASSWORD/a \ \ \ \ SERVER_SSL_KEY_STORE: /app/key1.p12\n \ \ \ \ SERVER_SSL_KEY_STORE_PASSWORD: abcd1234\n \ \ \ \ SERVER_SSL_KEY_STORE_TYPE: PKCS12\n \ \ \ \ SERVER_SSL_KEY_ALIAS: tomcat" $REMOTE_PATH/docker-compose.yml
  fi

  # Log the changes for verification
  echo "Modified docker-compose.yml:"
  grep "SPRING_DATASOURCE_USERNAME" $REMOTE_PATH/docker-compose.yml
  grep "SPRING_DATASOURCE_PASSWORD" $REMOTE_PATH/docker-compose.yml
  grep "SERVER_SSL_KEY_STORE" $REMOTE_PATH/docker-compose.yml

  # keystore 파일 확인
  if [ -f "$KEYSTORE_REMOTE_PATH" ]; then
    echo "key.p12 파일이 $REMOTE_PATH 경로에 존재합니다."
  else
    echo "key.p12 파일이 $REMOTE_PATH 경로에 존재하지 않습니다."
  fi

  sudo systemctl start docker
  cd $REMOTE_PATH
  sudo docker-compose -f docker-compose.yml down
  sudo docker-compose build --no-cache
  sudo docker-compose up -d
EOF

echo "Deployment to EC2 complete!"