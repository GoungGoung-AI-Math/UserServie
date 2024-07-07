#!/bin/bash

# Define variables
EC2_USER=ec2-user
EC2_IP=13.209.22.119
PEM_PATH=~/Desktop/aikey.pem
LOCAL_PATH=$(dirname "$0")
REMOTE_PATH=/home/$EC2_USER/userapp
TOKEN_PATH="/home/ec2-user/project-root/requirements/vault/certs/springUser/vault_token.txt" # Updated token path

# Create the remote directory if it doesn't exist
ssh -i $PEM_PATH $EC2_USER@$EC2_IP "mkdir -p $REMOTE_PATH"

# Copy files to EC2
echo "Copying files to EC2..."
scp -i $PEM_PATH -r $LOCAL_PATH/docker-compose.yml $LOCAL_PATH/Dockerfile $LOCAL_PATH/build/libs/Math.AI-0.0.1-SNAPSHOT.jar $TOKEN_PATH $EC2_USER@$EC2_IP:$REMOTE_PATH

# SSH into EC2 and run Docker Compose
echo "Connecting to EC2 and starting Docker Compose..."
ssh -t -i $PEM_PATH $EC2_USER@$EC2_IP << EOF
  VAULT_TOKEN=\$(cat $TOKEN_PATH)
  export VAULT_ADDR='http://127.0.0.1:8200'
  export VAULT_TOKEN

  echo "Using Vault token: \$VAULT_TOKEN"

  # Fetch secrets using Vault CLI
  echo "Fetching secrets using Vault CLI..."
  SECRETS=\$(vault kv get -format=json kv/db-creds)
  DB_USERNAME=\$(echo \$SECRETS | jq -r '.data.data.username_user_db')
  DB_PASSWORD=\$(echo \$SECRETS | jq -r '.data.data.password_user_db')
  echo "Database credentials - Username: \$DB_USERNAME, Password: \$DB_PASSWORD"

  # Modify docker-compose.yml directly to set environment variables
  sed -i "s/SPRING_DATASOURCE_USERNAME: springuser/SPRING_DATASOURCE_USERNAME: \$DB_USERNAME/" $REMOTE_PATH/docker-compose.yml
  sed -i "s/SPRING_DATASOURCE_PASSWORD: password/SPRING_DATASOURCE_PASSWORD: \$DB_PASSWORD/" $REMOTE_PATH/docker-compose.yml

  # Log the changes for verification
  echo "Modified docker-compose.yml:"
  grep "SPRING_DATASOURCE_USERNAME" $REMOTE_PATH/docker-compose.yml
  grep "SPRING_DATASOURCE_PASSWORD" $REMOTE_PATH/docker-compose.yml

  sudo systemctl start docker
  cd $REMOTE_PATH
  sudo /usr/local/bin/docker-compose -f docker-compose.yml down
  sudo /usr/local/bin/docker-compose -f docker-compose.yml up --build -d
EOF

echo "Deployment to EC2 complete!"
