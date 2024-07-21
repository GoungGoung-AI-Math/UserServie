# AVRO Model 중앙화

- KafkaInfra를 모든 레포지토리에서 subtree로 등록
  - `git remote add kafkaInfra https://github.com/GoungGoung-AI-Math/KafkaInfra.git`
  - `git subtree add --prefix my-kafka-infra kafkaInfra main `

- Avro 파일 수정
  - `gradle build generateAvro` 실행
  - 커밋 및 배포
  - 다른 레포지토리는 풀 받아서 사용


