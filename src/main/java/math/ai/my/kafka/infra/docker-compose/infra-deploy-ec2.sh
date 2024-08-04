#!/bin/bash

# 1. 공공 DNS 주소 가져오기
EC2_DNS=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID --filters "Name=instance-state-name,Values=running" "Name=tag-value,Values=my-kafka-cluster" --query 'Reservations[*].Instances[*].PublicDnsName' --output text)

echo "EC2 Public DNS: $EC2_DNS"

# 2. cluster docker file 업로드
scp -i "ec2-cluster-key.pem" -r ./cluster ec2-user@$EC2_DNS:/home/ec2-user/cluster

# 3. EC2에 접속 및 Docker Compose 실행
ssh -i "ec2-cluster-key.pem" ec2-user@$EC2_DNS << EOF

  # Verify Docker Compose installation
  for i in {1..5}
  do
      if docker-compose --version &> /dev/null
      then
          echo "docker-compose is installed and available"
          break
      else
          echo "docker-compose installation failed, retrying in 10 seconds..."
          sleep 10
      fi

      if [ $i -eq 5 ]; then
          echo "docker-compose installation failed after 5 attempts"
          exit 1
      fi
  done


  # Verify nc installation
  for i in {1..5}
  do
      if nc --version &> /dev/null
      then
          echo "nc is installed and available"
          break
      else
          echo "nc installation failed, retrying in 10 seconds..."
          sleep 10
      fi

      if [ $i -eq 5 ]; then
          echo "nc installation failed after 5 attempts"
          exit 1
      fi
  done

  # 이미지 다운로드
  docker pull confluentinc/cp-kafka:7.0.1
  docker pull hlebalbau/kafka-manager:stable
  docker pull confluentinc/cp-schema-registry:7.0.1
  docker pull confluentinc/cp-zookeeper:7.0.1

  # 다운로드 상태 확인
  echo "Checking Docker images..."
  docker images | grep -e "confluentinc/cp-kafka:7.0.1" -e "hlebalbau/kafka-manager:stable" -e "confluentinc/cp-schema-registry:7.0.1" -e "confluentinc/cp-zookeeper:7.0.1"

  echo "Docker image download complete."

  # 3. zookeeper 실행
  docker-compose -f /home/ec2-user/cluster/common.yml -f /home/ec2-user/cluster/zookeeper.yml up -d

  # 4. zookeeper healthcheck
  until echo ruok | nc localhost 2181 | grep imok; do
    echo "Waiting for Zookeeper to start..."
    sleep 10
  done
  echo "Zookeeper is up and running."

  # 5. kafka_cluster 실행
  docker-compose -f /home/ec2-user/cluster/common.yml -f /home/ec2-user/cluster/kafka_cluster.yml up -d

  # 6. kafka manager에 my-kafka-cluster라는 이름으로 "zookeeper:2181" 이라는 zookeeper url을 적용해서 kafka cluster를 등록하기
  curl -X POST "http://localhost:9000/clusters" --data "name=my-kafka-cluster&zkHosts=zookeeper:2181"

  # 7. kafka manager 상태 확인
  until [[ "$(curl -s http://localhost:9000/api/health)" == *"healthy"* ]]; do
    echo "Waiting for Kafka Manager to be healthy..."
    sleep 10
  done
  echo "Kafka Manager is healthy."

  # 8. kafka에 토픽 생성하기 - 처음 세팅할 때만 하면 된다.
  docker-compose -f /home/ec2-user/cluster/common.yml -f /home/ec2-user/cluster/init_kafka.yml up -d
EOF