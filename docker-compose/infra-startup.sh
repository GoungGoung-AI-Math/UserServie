#!/bin/bash

# 1. zookeeper 실행하기
echo "Starting ZooKeeper..."
docker-compose -f docker-compose/common.yml -f docker-compose/zookeeper.yml up -d

# 2. zookeeper 테스트하기
echo "Waiting for ZooKeeper to be ready..."
RETRY_COUNT=0
MAX_RETRIES=30
SLEEP_TIME=5

while [[ $RETRY_COUNT -lt $MAX_RETRIES ]]; do
    if echo ruok | nc localhost 2181 | grep -q imok; then
        echo "ZooKeeper is up and running!"
        break
    else
        echo "ZooKeeper is not ready yet. Retrying in $SLEEP_TIME seconds..."
        ((RETRY_COUNT++))
        sleep $SLEEP_TIME
    fi
done

if [[ $RETRY_COUNT -eq $MAX_RETRIES ]]; then
    echo "ZooKeeper did not start correctly. Exiting..."
    exit 1
fi

# 3. kafka 실행하기
echo "Starting Kafka brokers..."
docker-compose -f docker-compose/common.yml -f docker-compose/kafka_cluster.yml up -d

# kafka-broker-1와 kafka-broker-2가 실행 중인지 확인
RETRY_COUNT=0

while [[ $RETRY_COUNT -lt $MAX_RETRIES ]]; do
    if docker ps -a | grep -q kafka-broker-1 && docker ps -a | grep -q kafka-broker-2; then
        echo "Kafka brokers are up and running!"
        break
    else
        echo "Kafka brokers are not ready yet. Retrying in $SLEEP_TIME seconds..."
        ((RETRY_COUNT++))
        sleep $SLEEP_TIME
    fi
done

if [[ $RETRY_COUNT -eq $MAX_RETRIES ]]; then
    echo "Kafka brokers did not start correctly. Exiting..."
    exit 1
fi

# 4. kafka에 토픽 생성하기 - 처음 세팅할 때만 하면 된다.
echo "Creating Kafka topics..."
docker-compose -f docker-compose/common.yml -f docker-compose/init_kafka.yml up -d

echo "Kafka setup completed successfully!"