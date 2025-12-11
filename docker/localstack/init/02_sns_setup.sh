#!/bin/bash

# ==========================================
# SNS 토픽 생성 스크립트
# ==========================================
#
# [목적]
# 프로젝트에 필요한 SNS 토픽들을 생성합니다.
#
# [SNS란?]
# Simple Notification Service - Pub/Sub 메시징
# Publisher → Topic → Subscribers

# ------------------------------------------
# 환경 변수 불러오기
# ------------------------------------------
source /etc/localstack/init/ready.d/00_env_setup.sh

# ------------------------------------------
# 토픽 생성 명령어 형식
# ------------------------------------------
# TOPIC_ARN=$(awslocal sns create-topic \
#   --name [토픽 이름] \
#   --output text \
#   --query 'TopicArn')
# ex)
# CHAT_EVENTS_TOPIC_ARN=$(awslocal sns create-topic \
#   --name bebee-chat-events \
#   --output text \
#   --query 'TopicArn')




# ------------------------------------------
# 확인
# ------------------------------------------
# awslocal sns list-topics