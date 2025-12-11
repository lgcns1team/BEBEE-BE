#!/bin/bash

# ==========================================
# SQS 큐 생성 스크립트
# ==========================================
#
# [목적]
# 프로젝트에 필요한 모든 SQS 큐를 생성합니다.
#
# [의존성]
# 00-env-setup.sh에 정의된 환경 변수가 필요합니다.

# ------------------------------------------
# 환경 변수 불러오기
# ------------------------------------------
#
source /etc/localstack/init/ready.d/00_env_setup.sh

# ------------------------------------------
# 큐 생성 명령어 형식
# ------------------------------------------
# awslocal sqs create-queue \
#   --queue-name [큐 이름] \
#   --attributes '{
#     "VisibilityTimeout": "30",              # 메시지 처리 시간 (초)
#     "MessageRetentionPeriod": "345600",     # 보관 기간 (4일)
#     "ReceiveMessageWaitTimeSeconds": "20"   # Long Polling (초)
#   }'

# ------------------------------------------
# 1. Dead Letter Queue (DLQ)
# ------------------------------------------
# 처리 실패한 메시지를 모아두는 큐
#
# awslocal sqs create-queue \
#   --queue-name bebee-dlq \
#   --attributes '{"MessageRetentionPeriod": "1209600"}'

# ------------------------------------------
# DLQ 연결 (선택사항)
# ------------------------------------------
# 3번 실패 시 DLQ로 이동
#
# DLQ_ARN="arn:aws:sqs:ap-northeast-2:000000000000:bebee-dlq"
# QUEUE_URL="http://localhost:4566/000000000000/bebee-chat-message-queue"
#
# awslocal sqs set-queue-attributes \
#   --queue-url "$QUEUE_URL" \
#   --attributes '{"RedrivePolicy":"{\"deadLetterTargetArn\":\"'$DLQ_ARN'\",\"maxReceiveCount\":\"3\"}"}'




# ------------------------------------------
# 확인
# ------------------------------------------
# awslocal sqs list-queues
