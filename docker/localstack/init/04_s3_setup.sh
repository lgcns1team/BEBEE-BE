#!/bin/bash

# ==========================================
# S3 버킷 생성 스크립트
# ==========================================
#
# [목적]
# 파일 저장을 위한 S3 버킷들을 생성합니다.
#
# [S3란?]
# Simple Storage Service - 객체 스토리지


# ------------------------------------------
# 환경 변수 불러오기
# ------------------------------------------
source /etc/localstack/init/ready.d/00_env_setup.sh


# ------------------------------------------
# 버킷 생성 명령어 형식
# ------------------------------------------
# awslocal s3api create-bucket \
#   --bucket [버킷 이름] \
#   --create-bucket-configuration LocationConstraint=ap-northeast-2

# ------------------------------------------
# 퍼블릭 읽기 권한 설정 (선택사항)
# ------------------------------------------
# 채팅 미디어는 앱에서 직접 접근 가능하도록 설정
#
# awslocal s3api put-bucket-policy \
#   --bucket bebee-chat-media-local \
#   --policy '{
#     "Version": "2012-10-17",
#     "Statement": [{
#       "Effect": "Allow",
#       "Principal": "*",
#       "Action": "s3:GetObject",
#       "Resource": "arn:aws:s3:::bebee-chat-media-local/*"
#     }]
#   }'


# ------------------------------------------
# CORS 설정 (선택사항)
# ------------------------------------------
# 웹 브라우저에서 S3 접근 허용
#
# awslocal s3api put-bucket-cors \
#   --bucket bebee-chat-media-local \
#   --cors-configuration '{
#     "CORSRules": [{
#       "AllowedOrigins": ["*"],
#       "AllowedMethods": ["GET", "PUT", "POST"],
#       "AllowedHeaders": ["*"],
#       "MaxAgeSeconds": 3000
#     }]
#   }'


# ------------------------------------------
# 확인
# ------------------------------------------
# awslocal s3 ls