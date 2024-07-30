#!/bin/bash

# 변수 정의
EC2_USER="ec2-user"
EC2_IP="3.38.213.181"
PEM_PATH="~/Desktop/aikey.pem"
LOCAL_PATH=$(dirname "$0")
REMOTE_PATH="/home/$EC2_USER/docker_project"
DOCKER_COMPOSE_FILE="docker-compose.yml"
AVRO_SCHEMAS_DIR="/Users/makisekurisu/Desktop/kafka/KafkaInfra/avro"
SCHEMA_REGISTRY_URL="http://3.38.213.181:8081"

# Docker Compose 파일이 현재 디렉토리에 존재하는지 확인
if [ ! -f "$LOCAL_PATH/$DOCKER_COMPOSE_FILE" ]; then
  echo "Error: $DOCKER_COMPOSE_FILE 파일이 현재 디렉토리에 존재하지 않습니다."
  exit 1
fi

# EC2 인스턴스에 연결하여 Docker 및 Docker Compose 설치
ssh -i $PEM_PATH $EC2_USER@$EC2_IP << EOF
# 시스템 업데이트
sudo yum update -y

# Docker 설치
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $EC2_USER

# Docker Compose 설치
DOCKER_COMPOSE_VERSION="v2.6.1"
sudo curl -L "https://github.com/docker/compose/releases/download/\$DOCKER_COMPOSE_VERSION/docker-compose-\$(uname -s)-\$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Docker 및 Docker Compose 버전 확인
docker --version
/usr/local/bin/docker-compose --version

# 프로젝트 디렉토리 생성
mkdir -p $REMOTE_PATH
EOF

# Docker Compose 파일을 EC2 인스턴스로 업로드
scp -i $PEM_PATH $LOCAL_PATH/$DOCKER_COMPOSE_FILE $EC2_USER@$EC2_IP:$REMOTE_PATH/

# EC2 인스턴스에 연결하여 Docker Compose를 사용하여 서비스를 시작
ssh -i $PEM_PATH $EC2_USER@$EC2_IP << EOF
cd $REMOTE_PATH
sudo docker-compose build --no-cache
sudo docker-compose up -d
EOF

echo "Docker Compose 서비스가 EC2 인스턴스에서 시작되었습니다."

# Schema Registry에 스키마 등록 함수 정의
register_schema() {
  schema_file=$1
  subject_name=$(basename "$schema_file" .avsc)

  # 스키마가 이미 등록되어 있는지 확인
  if curl -s -o /dev/null -w "%{http_code}" "$SCHEMA_REGISTRY_URL/subjects/$subject_name-value/versions/latest" | grep -q "200"; then
    echo "Schema for subject $subject_name already exists. Skipping..."
  else
    # 스키마 등록
    schema=$(jq -Rs '.' < "$schema_file")
    response=$(curl -s -w "%{http_code}" -o /dev/null -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
      --data "{\"schema\": $schema}" \
      "$SCHEMA_REGISTRY_URL/subjects/$subject_name-value/versions")

    if [ "$response" -eq 200 ] || [ "$response" -eq 201 ]; then
      echo "Schema for subject $subject_name registered successfully."
    else
      echo "Failed to register schema for subject $subject_name. Response code: $response"
    fi
  fi
}

# /Users/makisekurisu/Desktop/kafka/KafkaInfra/avro 디렉토리의 모든 스키마 파일을 Schema Registry에 등록
for schema_file in $AVRO_SCHEMAS_DIR/*.avsc; do
  register_schema "$schema_file"
done

echo "Schema registration complete."
