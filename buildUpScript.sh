#!/bin/bash

# EC2 인스턴스에 접속하여 vault_token.txt 파일 읽기
VAULT_TOKEN=$(ssh -i ~/Desktop/aikey.pem ec2-user@13.209.22.119 'cat /home/ec2-user/userapp/vault_token.txt')

# Vault URL 설정
VAULT_URL="https://test.udongrang.com:8200"  # Vault 서버 URL을 실제 값으로 변경하세요

# Vault에서 구글 클라이언트 아이디와 시크릿 가져오기
secrets=$(curl -s -k -X GET "${VAULT_URL}/v1/kv/data/db-creds" -H "X-Vault-Token: ${VAULT_TOKEN}")
username_user_db=$(echo $secrets | jq -r '.data.data.username_user_db')
password_user_db=$(echo $secrets | jq -r '.data.data.password_user_db')
google_client_id=$(echo $secrets | jq -r '.data.data.google_client_id')
google_client_secret=$(echo $secrets | jq -r '.data.data.google_client_secret')

# application.yml 파일에 설정 추가
application_yml_path="/Users/makisekurisu/Desktop/User.AI/src/main/resources/application.yml"  # 실제 프로젝트 경로로 변경하세요

echo "Updating application.yml..."

# 데이터베이스 설정 업데이트
yq e -i "
  .spring.datasource.url = \"jdbc:postgresql://13.209.22.119:5434/springdb_user\" |
  .spring.datasource.username = \"$username_user_db\" |
  .spring.datasource.password = \"$password_user_db\" |
  .spring.datasource.driver-class-name = \"org.postgresql.Driver\"
" $application_yml_path

# 구글 클라이언트 설정 업데이트
yq e -i "
  .google.client.id = \"$google_client_id\" |
  .google.client.secret = \"$google_client_secret\"
" $application_yml_path

echo "application.yml updated successfully."
