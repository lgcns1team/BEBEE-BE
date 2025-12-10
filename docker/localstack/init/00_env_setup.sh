#!/bin/bash

# ==========================================
# 환경 변수 설정 스크립트
# ==========================================
#
# [목적]
# 모든 초기화 스크립트에서 공통으로 사용할 환경 변수를 설정합니다.

# ------------------------------------------
# AWS CLI 기본 설정
# ------------------------------------------
#
# [LocalStack 엔드포인트]
# LocalStack은 모든 AWS 서비스를 단일 엔드포인트(4566 포트)로 제공합니다.
# 실제 AWS는 서비스별로 다른 엔드포인트를 사용하지만, LocalStack은 하나로 통일됩니다.
#
# [AWS 리전 설정]
# 실제 AWS와 동일한 리전을 사용하는 것이 좋습니다.
# ap-northeast-2 = 서울 리전
#
# [AWS 자격증명]
# LocalStack은 실제 자격증명 검증을 하지 않습니다.
# 더미 값(test)을 사용해도 정상 작동합니다.
# 실제 AWS에서는 반드시 유효한 Access Key와 Secret Key가 필요합니다.

export AWS_ENDPOINT_URL=http://localhost:4566
export AWS_DEFAULT_REGION=ap-northeast-2
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test

# ------------------------------------------
# 프로젝트 관련 변수
# ------------------------------------------
#
# [프로젝트 이름]
# 리소스 이름의 접두사로 사용됩니다.
# 예: bebee-chat-message-queue, bebee-chat-events
#
# [환경 구분]
# 개발(dev), 스테이징(staging), 운영(prod) 환경을 구분합니다.
# LocalStack에서는 보통 local 또는 dev를 사용합니다.
#
# [LocalStack 계정 ID]
# LocalStack은 항상 더미 계정 ID를 사용합니다.
# 실제 AWS에서는 각 계정마다 고유한 12자리 숫자가 부여됩니다.

export PROJECT_NAME=bebee
export ENVIRONMENT=local
export AWS_ACCOUNT_ID=000000000000