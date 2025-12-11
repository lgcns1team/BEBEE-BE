#!/bin/bash

# ==========================================
# SNS → SQS 구독 설정 스크립트
# ==========================================
#
# [목적]
# SNS 토픽을 SQS 큐로 연결합니다.
#
# [동작]
# SNS 토픽에 메시지 발행 → 자동으로 SQS 큐에 전달

# ------------------------------------------
# 환경 변수 불러오기
# ------------------------------------------
source /etc/localstack/init/ready.d/00_env_setup.sh

# ------------------------------------------
# SQS 큐 ARN 가져오기
# ------------------------------------------
# SNS가 SQS로 메시지를 보내려면 큐의 ARN이 필요합니다.
#
# CHAT_QUEUE_ARN=$(awslocal sqs get-queue-attributes \
#   --queue-url http://localhost:4566/000000000000/bebee-chat-message-queue \
#   --attribute-names QueueArn \
#   --output text \
#   --query 'Attributes.QueueArn')

# ------------------------------------------
# SNS 토픽 ARN (02-sns-setup.sh에서 생성)
# ------------------------------------------

# ------------------------------------------
# 구독 생성 명령어 형식
# ------------------------------------------
# awslocal sns subscribe \
#   --topic-arn [SNS 토픽 ARN] \
#   --protocol sqs \
#   --notification-endpoint [SQS 큐 ARN]

# ------------------------------------------
# 확인
# ------------------------------------------
# awslocal sns list-subscriptions-by-topic \
#   --topic-arn $CHAT_EVENTS_TOPIC_ARN