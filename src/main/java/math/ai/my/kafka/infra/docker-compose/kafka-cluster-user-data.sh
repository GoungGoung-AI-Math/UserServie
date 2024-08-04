#!/bin/bash

# Update the package index
sudo yum update -y

# Install Docker
sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# Download Docker Compose
sudo curl -SL https://github.com/docker/compose/releases/download/v2.19.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose

# Apply executable permissions to the binary
sudo chmod +x /usr/local/bin/docker-compose

# Install nc
sudo yum install -y nmap-ncat

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


# Verify Docker Compose installation
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