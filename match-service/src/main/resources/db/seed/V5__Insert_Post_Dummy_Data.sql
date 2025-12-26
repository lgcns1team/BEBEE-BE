-- ========================================
-- Post 더미 데이터
-- ========================================
-- 개발 및 테스트 환경에서 사용할 게시글 데이터 (100개)

-- Post 1-10: 외출동행 (최근 1-3일 전 게시글)
INSERT INTO post (post_id, member_id, title, type, unit_honey, total_honey, region, status, legal_dong_code, latitude, longitude, applicant_count, content, created_at, updated_at)
VALUES
(1001, 100, '병원 동행 도와주실 분', 'DAY', 5000, 5000, '서울시 강남구 역삼동', 'NON_MATCHED', '1168010100', 37.5012, 127.0396, 0, '정기 병원 진료 갈 때 함께 가주실 분을 찾습니다.', NOW() - INTERVAL 3 HOUR, NOW()),
(1002, 200, '주말 공원 산책 동행', 'DAY', 3000, 3000, '서울시 서초구 서초동', 'NON_MATCHED', '1165010100', 37.4837, 127.0324, 2, '휠체어로 공원 산책하실 때 도와주세요.', NOW() - INTERVAL 8 HOUR, NOW()),
(1003, 100, '문화센터 수업 동행', 'TERM', 4000, 40000, '서울시 송파구 잠실동', 'NON_MATCHED', '1171010100', 37.5133, 127.1000, 1, '주 2회 문화센터 수업 동행해주실 분 구합니다.', NOW() - INTERVAL 15 HOUR, NOW()),
(1004, 300, '마트 장보기 동행', 'DAY', 5000, 5000, '서울시 강동구 천호동', 'NON_MATCHED', '1174010100', 37.5388, 127.1236, 0, '주 1회 마트 장보기 도와주세요.', NOW() - INTERVAL 1 DAY, NOW()),
(1005, 200, '도서관 이용 도움', 'DAY', 3500, 3500, '서울시 광진구 구의동', 'NON_MATCHED', '1121510100', 37.5384, 127.0857, 3, '도서관 가서 책 찾는 것 도와주실 분.', NOW() - INTERVAL 1 DAY - INTERVAL 5 HOUR, NOW()),
(1006, 100, '은행 업무 동행', 'DAY', 4500, 4500, '서울시 성동구 성수동1가', 'PROCEEDING', '1120010100', 37.5443, 127.0557, 5, '은행 업무 볼 때 동행해주세요.', NOW() - INTERVAL 1 DAY - INTERVAL 12 HOUR, NOW()),
(1007, 300, '관공서 방문 동행', 'DAY', 5000, 5000, '서울시 중랑구 면목동', 'NON_MATCHED', '1126010100', 37.5833, 127.0913, 1, '구청 방문 시 동행 필요합니다.', NOW() - INTERVAL 2 DAY, NOW()),
(1008, 200, '정기 통원 치료 동행', 'TERM', 6000, 60000, '서울시 동대문구 회기동', 'NON_MATCHED', '1123010100', 37.5894, 127.0582, 0, '주 2회 물리치료 받으러 갈 때 도와주세요.', NOW() - INTERVAL 2 DAY - INTERVAL 6 HOUR, NOW()),
(1009, 100, '쇼핑몰 쇼핑 동행', 'DAY', 4000, 4000, '서울시 성북구 정릉동', 'NON_MATCHED', '1129010100', 37.6106, 127.0099, 2, '백화점 쇼핑 시 동행해주실 분.', NOW() - INTERVAL 2 DAY - INTERVAL 18 HOUR, NOW()),
(1010, 300, '미용실 방문 동행', 'DAY', 3500, 3500, '서울시 강북구 수유동', 'NON_MATCHED', '1130510100', 37.6388, 127.0255, 0, '월 1회 미용실 갈 때 함께 가주세요.', NOW() - INTERVAL 3 DAY, NOW()),

-- Post 11-20: 방문목욕 (4-6일 전 게시글)
(1011, 200, '주 2회 방문 목욕 서비스', 'TERM', 8000, 80000, '서울시 도봉구 쌍문동', 'NON_MATCHED', '1132010100', 37.6500, 127.0322, 1, '화, 목 저녁 방문 목욕 도움 필요합니다.', NOW() - INTERVAL 4 DAY, NOW()),
(1012, 100, '주말 방문 목욕', 'DAY', 10000, 10000, '서울시 노원구 상계동', 'NON_MATCHED', '1135010100', 37.6541, 127.0658, 0, '토요일 오전 방문 목욕 서비스 요청합니다.', NOW() - INTERVAL 4 DAY - INTERVAL 6 HOUR, NOW()),
(1013, 300, '어르신 목욕 보조', 'DAY', 12000, 12000, '서울시 은평구 불광동', 'PROCEEDING', '1138010100', 37.6107, 126.9296, 4, '거동 불편하신 할머니 목욕 도와주세요.', NOW() - INTERVAL 4 DAY - INTERVAL 14 HOUR, NOW()),
(1014, 200, '장애인 샤워 도움', 'DAY', 9000, 9000, '서울시 서대문구 연희동', 'NON_MATCHED', '1141010100', 37.5674, 126.9346, 2, '휠체어 사용자 샤워 보조 필요합니다.', NOW() - INTERVAL 5 DAY, NOW()),
(1015, 100, '방문 목욕 정기 서비스', 'TERM', 8500, 85000, '서울시 마포구 상암동', 'NON_MATCHED', '1144011000', 37.5793, 126.8895, 0, '주 3회 정기 방문 목욕 서비스 신청합니다.', NOW() - INTERVAL 5 DAY - INTERVAL 8 HOUR, NOW()),
(1016, 300, '주중 오전 목욕 도움', 'DAY', 9500, 9500, '서울시 양천구 목동', 'NON_MATCHED', '1147510100', 37.5263, 126.8750, 1, '월, 수, 금 오전 목욕 보조 요청.', NOW() - INTERVAL 5 DAY - INTERVAL 16 HOUR, NOW()),
(1017, 200, '전신 목욕 서비스', 'DAY', 11000, 11000, '서울시 강서구 화곡동', 'NON_MATCHED', '1150010100', 37.5410, 126.8496, 0, '전신 목욕 가능하신 분 찾습니다.', NOW() - INTERVAL 6 DAY, NOW()),
(1018, 100, '침상 목욕 도움', 'DAY', 13000, 13000, '서울시 구로구 구로동', 'NON_MATCHED', '1153010100', 37.4954, 126.8874, 3, '침대에서 목욕하는 서비스 필요해요.', NOW() - INTERVAL 6 DAY - INTERVAL 10 HOUR, NOW()),
(1019, 300, '반신마비 환자 목욕', 'DAY', 12000, 12000, '서울시 금천구 시흥동', 'PROCEEDING', '1154510100', 37.4567, 126.8958, 5, '반신마비 환자 목욕 보조 가능하신 분.', NOW() - INTERVAL 6 DAY - INTERVAL 18 HOUR, NOW()),
(1020, 200, '장기 목욕 서비스', 'TERM', 9000, 90000, '서울시 영등포구 여의도동', 'NON_MATCHED', '1156010100', 37.5219, 126.9245, 0, '3개월간 주 2회 목욕 서비스 계약합니다.', NOW() - INTERVAL 7 DAY, NOW()),

-- Post 21-30: 방문간호 (8-10일 전 게시글)
(1021, 100, '당뇨 환자 혈당 체크', 'TERM', 7000, 70000, '서울시 동작구 노량진동', 'NON_MATCHED', '1159010100', 37.5112, 126.9428, 2, '매일 아침 혈당 체크 및 인슐린 주사 도움.', NOW() - INTERVAL 8 DAY, NOW()),
(1022, 300, '상처 드레싱 케어', 'DAY', 8000, 8000, '서울시 관악구 봉천동', 'NON_MATCHED', '1162010100', 37.4820, 126.9519, 0, '욕창 상처 드레싱 간호 필요합니다.', NOW() - INTERVAL 8 DAY - INTERVAL 8 HOUR, NOW()),
(1023, 200, '혈압 측정 서비스', 'DAY', 5000, 5000, '서울시 강남구 삼성동', 'NON_MATCHED', '1168010700', 37.5087, 127.0632, 1, '주 3회 혈압 측정 및 기록 부탁드려요.', NOW() - INTERVAL 8 DAY - INTERVAL 16 HOUR, NOW()),
(1024, 100, '투약 관리 도움', 'TERM', 6000, 60000, '서울시 서초구 반포동', 'PROCEEDING', '1165010200', 37.5048, 127.0028, 4, '아침 저녁 약 복용 관리 필요합니다.', NOW() - INTERVAL 9 DAY, NOW()),
(1025, 300, '방문 물리치료', 'DAY', 10000, 10000, '서울시 송파구 문정동', 'NON_MATCHED', '1171011000', 37.4868, 127.1225, 0, '주 2회 방문 물리치료 서비스 요청.', NOW() - INTERVAL 9 DAY - INTERVAL 6 HOUR, NOW()),
(1026, 200, '도뇨관 관리', 'DAY', 9000, 9000, '서울시 강동구 암사동', 'NON_MATCHED', '1174010200', 37.5503, 127.1286, 2, '도뇨관 교체 및 관리 도움 필요.', NOW() - INTERVAL 9 DAY - INTERVAL 14 HOUR, NOW()),
(1027, 100, '산소포화도 체크', 'TERM', 5500, 55000, '서울시 광진구 자양동', 'NON_MATCHED', '1121510200', 37.5352, 127.0694, 1, '매일 산소포화도 측정 및 기록.', NOW() - INTERVAL 10 DAY, NOW()),
(1028, 300, '욕창 관리 간호', 'DAY', 11000, 11000, '서울시 성동구 행당동', 'NON_MATCHED', '1120010200', 37.5599, 127.0362, 0, '욕창 예방 및 관리 간호 서비스.', NOW() - INTERVAL 10 DAY - INTERVAL 8 HOUR, NOW()),
(1029, 200, '위루관 영양 주입', 'DAY', 8500, 8500, '서울시 중랑구 상봉동', 'PROCEEDING', '1126010200', 37.5964, 127.0858, 3, '위루관 통한 영양 주입 도움 필요.', NOW() - INTERVAL 10 DAY - INTERVAL 16 HOUR, NOW()),
(1030, 100, '기관지 흡인 케어', 'DAY', 12000, 12000, '서울시 동대문구 장안동', 'NON_MATCHED', '1123010200', 37.5714, 127.0483, 0, '기관지 흡인 처치 가능하신 분.', NOW() - INTERVAL 11 DAY, NOW()),

-- Post 31-40: 가사지원 (12-14일 전 게시글)
(1031, 300, '주 3회 청소 도움', 'TERM', 4500, 45000, '서울시 성북구 종암동', 'NON_MATCHED', '1129010200', 37.5956, 127.0275, 1, '월, 수, 금 오전 집안 청소 도와주세요.', NOW() - INTERVAL 12 DAY, NOW()),
(1032, 200, '빨래 및 다림질', 'DAY', 5000, 5000, '서울시 강북구 미아동', 'NON_MATCHED', '1130510200', 37.6131, 127.0256, 2, '주 1회 빨래와 다림질 서비스.', NOW() - INTERVAL 12 DAY - INTERVAL 8 HOUR, NOW()),
(1033, 100, '설거지 도움', 'DAY', 3000, 3000, '서울시 도봉구 방학동', 'NON_MATCHED', '1132010200', 37.6684, 127.0394, 0, '매일 저녁 설거지 도와주실 분.', NOW() - INTERVAL 12 DAY - INTERVAL 16 HOUR, NOW()),
(1034, 300, '정리정돈 서비스', 'DAY', 4000, 4000, '서울시 노원구 중계동', 'PROCEEDING', '1135010200', 37.6397, 127.0734, 5, '집안 정리정돈 및 청소 요청합니다.', NOW() - INTERVAL 13 DAY, NOW()),
(1035, 200, '주방 청소 도움', 'DAY', 4500, 4500, '서울시 은평구 응암동', 'NON_MATCHED', '1138010200', 37.6017, 126.9185, 1, '주 2회 주방 청소 서비스 필요.', NOW() - INTERVAL 13 DAY - INTERVAL 6 HOUR, NOW()),
(1036, 100, '화장실 청소', 'DAY', 4000, 4000, '서울시 서대문구 홍제동', 'NON_MATCHED', '1141010200', 37.5866, 126.9467, 0, '주 2회 화장실 청소 부탁드려요.', NOW() - INTERVAL 13 DAY - INTERVAL 12 HOUR, NOW()),
(1037, 300, '이불 빨래 서비스', 'DAY', 6000, 6000, '서울시 마포구 공덕동', 'NON_MATCHED', '1144011100', 37.5445, 126.9516, 2, '월 1회 이불 빨래 도와주세요.', NOW() - INTERVAL 13 DAY - INTERVAL 18 HOUR, NOW()),
(1038, 200, '창문 청소', 'DAY', 5000, 5000, '서울시 양천구 신정동', 'NON_MATCHED', '1147510200', 37.5247, 126.8563, 0, '분기별 창문 청소 서비스.', NOW() - INTERVAL 14 DAY, NOW()),
(1039, 100, '환기 및 먼지 청소', 'TERM', 3500, 35000, '서울시 강서구 등촌동', 'NON_MATCHED', '1150010200', 37.5509, 126.8646, 1, '주 2회 환기 및 먼지 청소.', NOW() - INTERVAL 14 DAY - INTERVAL 6 HOUR, NOW()),
(1040, 300, '베란다 정리', 'DAY', 4500, 4500, '서울시 구로구 신도림동', 'PROCEEDING', '1153010200', 37.5088, 126.8895, 3, '베란다 정리 및 청소 요청.', NOW() - INTERVAL 14 DAY - INTERVAL 12 HOUR, NOW()),

-- Post 41-50: 식사도움 (15-17일 전 게시글)
(1041, 200, '아침 식사 준비', 'TERM', 5000, 50000, '서울시 금천구 독산동', 'NON_MATCHED', '1154510200', 37.4693, 126.8969, 0, '평일 아침 식사 준비 도와주세요.', NOW() - INTERVAL 15 DAY, NOW()),
(1042, 100, '점심 도시락 만들기', 'DAY', 6000, 6000, '서울시 영등포구 신길동', 'NON_MATCHED', '1156010200', 37.5085, 126.9144, 2, '주 5회 점심 도시락 준비 필요.', NOW() - INTERVAL 15 DAY - INTERVAL 7 HOUR, NOW()),
(1043, 300, '저녁 식사 보조', 'DAY', 4500, 4500, '서울시 동작구 상도동', 'NON_MATCHED', '1159010200', 37.5025, 126.9495, 1, '저녁 식사 먹을 때 도와주세요.', NOW() - INTERVAL 15 DAY - INTERVAL 14 HOUR, NOW()),
(1044, 200, '반찬 만들기', 'DAY', 7000, 7000, '서울시 관악구 신림동', 'PROCEEDING', '1162010200', 37.4844, 126.9298, 4, '주 1회 반찬 만들기 도움.', NOW() - INTERVAL 16 DAY, NOW()),
(1045, 100, '식사 보조', 'TERM', 4000, 40000, '서울시 강남구 대치동', 'NON_MATCHED', '1168010400', 37.4940, 127.0626, 0, '하루 3끼 식사 보조 필요합니다.', NOW() - INTERVAL 16 DAY - INTERVAL 6 HOUR, NOW()),
(1046, 300, '유동식 준비', 'DAY', 5500, 5500, '서울시 서초구 양재동', 'NON_MATCHED', '1165010600', 37.4708, 127.0453, 1, '유동식 만들어주실 분 구합니다.', NOW() - INTERVAL 16 DAY - INTERVAL 12 HOUR, NOW()),
(1047, 200, '영양식 조리', 'DAY', 6500, 6500, '서울시 송파구 가락동', 'NON_MATCHED', '1171010500', 37.4925, 127.1184, 0, '당뇨식 조리 가능하신 분.', NOW() - INTERVAL 16 DAY - INTERVAL 18 HOUR, NOW()),
(1048, 100, '식재료 손질', 'DAY', 4000, 4000, '서울시 강동구 고덕동', 'NON_MATCHED', '1174010500', 37.5548, 127.1544, 2, '장 본 식재료 손질 도와주세요.', NOW() - INTERVAL 17 DAY, NOW()),
(1049, 300, '간식 준비', 'DAY', 3500, 3500, '서울시 광진구 중곡동', 'PROCEEDING', '1121510300', 37.5634, 127.0834, 3, '오후 간식 준비 부탁드립니다.', NOW() - INTERVAL 17 DAY - INTERVAL 8 HOUR, NOW()),
(1050, 200, '특별식 조리', 'DAY', 8000, 8000, '서울시 성동구 왕십리동', 'NON_MATCHED', '1120010300', 37.5633, 127.0371, 0, '저염식 조리 가능하신 분.', NOW() - INTERVAL 17 DAY - INTERVAL 16 HOUR, NOW()),

-- Post 51-60: 학습지원 (18-20일 전 게시글)
(1051, 100, '영어 회화 연습', 'TERM', 6000, 60000, '서울시 중랑구 묵동', 'NON_MATCHED', '1126010300', 37.6104, 127.0729, 1, '주 2회 영어 회화 연습 파트너.', NOW() - INTERVAL 18 DAY, NOW()),
(1052, 300, '컴퓨터 활용 교육', 'DAY', 5500, 5500, '서울시 동대문구 이문동', 'NON_MATCHED', '1123010300', 37.5945, 127.0586, 0, '컴퓨터 기초 사용법 알려주세요.', NOW() - INTERVAL 18 DAY - INTERVAL 7 HOUR, NOW()),
(1053, 200, '스마트폰 사용법', 'DAY', 4500, 4500, '서울시 성북구 길음동', 'PROCEEDING', '1129010300', 37.6036, 127.0261, 4, '스마트폰 기본 기능 교육 필요.', NOW() - INTERVAL 18 DAY - INTERVAL 14 HOUR, NOW()),
(1054, 100, '독서 도움', 'DAY', 4000, 4000, '서울시 강북구 번동', 'NON_MATCHED', '1130510300', 37.6384, 127.0310, 2, '책 읽어주실 분 찾습니다.', NOW() - INTERVAL 19 DAY, NOW()),
(1055, 300, '점자 학습 지원', 'TERM', 7000, 70000, '서울시 도봉구 창동', 'NON_MATCHED', '1132010300', 37.6533, 127.0471, 0, '점자 배우는 것 도와주세요.', NOW() - INTERVAL 19 DAY - INTERVAL 6 HOUR, NOW()),
(1056, 200, '수화 배우기', 'DAY', 6500, 6500, '서울시 노원구 월계동', 'NON_MATCHED', '1135010300', 37.6178, 127.0583, 1, '수화 기초 가르쳐주실 분.', NOW() - INTERVAL 19 DAY - INTERVAL 12 HOUR, NOW()),
(1057, 100, '그림 그리기 교육', 'DAY', 5000, 5000, '서울시 은평구 녹번동', 'NON_MATCHED', '1138010300', 37.6026, 126.9278, 0, '취미로 그림 그리기 배우고 싶어요.', NOW() - INTERVAL 19 DAY - INTERVAL 18 HOUR, NOW()),
(1058, 300, '음악 감상 지도', 'DAY', 4500, 4500, '서울시 서대문구 북가좌동', 'PROCEEDING', '1141010300', 37.5784, 126.9133, 3, '클래식 음악 감상 함께해요.', NOW() - INTERVAL 20 DAY, NOW()),
(1059, 200, '악기 연주 배우기', 'TERM', 8000, 80000, '서울시 마포구 아현동', 'NON_MATCHED', '1144011200', 37.5550, 126.9563, 0, '피아노 기초 배우고 싶습니다.', NOW() - INTERVAL 20 DAY - INTERVAL 8 HOUR, NOW()),
(1060, 100, '글쓰기 연습', 'DAY', 5500, 5500, '서울시 양천구 신월동', 'NON_MATCHED', '1147510300', 37.5177, 126.8349, 2, '일기 쓰기 연습 도와주세요.', NOW() - INTERVAL 20 DAY - INTERVAL 16 HOUR, NOW()),

-- Post 61-70: 정서적 지원 (21-23일 전 게시글)
(1061, 300, '대화 상대', 'DAY', 3000, 3000, '서울시 강서구 가양동', 'NON_MATCHED', '1150010300', 37.5612, 126.8548, 1, '말벗이 되어주실 분 찾아요.', NOW() - INTERVAL 21 DAY, NOW()),
(1062, 200, '산책 동반자', 'DAY', 3500, 3500, '서울시 구로구 오류동', 'PROCEEDING', '1153010300', 37.4937, 126.8474, 5, '매일 오후 산책 함께해요.', NOW() - INTERVAL 21 DAY - INTERVAL 7 HOUR, NOW()),
(1063, 100, '게임 상대', 'DAY', 3000, 3000, '서울시 금천구 가산동', 'NON_MATCHED', '1154510300', 37.4817, 126.8906, 0, '보드게임 함께 할 분 구해요.', NOW() - INTERVAL 21 DAY - INTERVAL 14 HOUR, NOW()),
(1064, 300, '영화 감상 동행', 'DAY', 4000, 4000, '서울시 영등포구 당산동', 'NON_MATCHED', '1156010300', 37.5341, 126.8963, 2, '주말 영화 보러 갈 친구 찾아요.', NOW() - INTERVAL 22 DAY, NOW()),
(1065, 200, '취미 활동 함께하기', 'TERM', 3500, 35000, '서울시 동작구 흑석동', 'NON_MATCHED', '1159010300', 37.5070, 126.9609, 1, '뜨개질 함께 하실 분.', NOW() - INTERVAL 22 DAY - INTERVAL 6 HOUR, NOW()),
(1066, 100, '라디오 청취 동반', 'DAY', 2500, 2500, '서울시 관악구 남현동', 'NON_MATCHED', '1162010300', 37.4770, 126.9816, 0, '오전 라디오 들으며 수다 떨어요.', NOW() - INTERVAL 22 DAY - INTERVAL 12 HOUR, NOW()),
(1067, 300, '종교 활동 동행', 'DAY', 4000, 4000, '서울시 강남구 청담동', 'PROCEEDING', '1168011000', 37.5196, 127.0486, 4, '주일 예배 함께 가주세요.', NOW() - INTERVAL 22 DAY - INTERVAL 18 HOUR, NOW()),
(1068, 200, '운동 보조', 'DAY', 4500, 4500, '서울시 서초구 잠원동', 'NON_MATCHED', '1165010300', 37.5137, 127.0124, 1, '재활 운동 함께 해주실 분.', NOW() - INTERVAL 23 DAY, NOW()),
(1069, 100, '정원 가꾸기', 'DAY', 5000, 5000, '서울시 송파구 풍납동', 'NON_MATCHED', '1171010200', 37.5304, 127.1189, 0, '베란다 화분 가꾸기 도와주세요.', NOW() - INTERVAL 23 DAY - INTERVAL 8 HOUR, NOW()),
(1070, 300, '애완동물 산책', 'TERM', 4000, 40000, '서울시 강동구 명일동', 'NON_MATCHED', '1174010300', 37.5511, 127.1476, 2, '강아지 산책 대신 해주실 분.', NOW() - INTERVAL 23 DAY - INTERVAL 16 HOUR, NOW()),

-- Post 71-80: 기타생활지원 (24-26일 전 게시글)
(1071, 200, '우편물 확인', 'DAY', 3000, 3000, '서울시 광진구 화양동', 'NON_MATCHED', '1121510400', 37.5439, 127.0712, 0, '주 1회 우편물 확인 및 정리.', NOW() - INTERVAL 24 DAY, NOW()),
(1072, 100, '택배 수령 대행', 'DAY', 2500, 2500, '서울시 성동구 옥수동', 'PROCEEDING', '1120010400', 37.5405, 127.0181, 3, '택배 받아주실 분 찾아요.', NOW() - INTERVAL 24 DAY - INTERVAL 7 HOUR, NOW()),
(1073, 300, '쓰레기 배출', 'DAY', 3500, 3500, '서울시 중랑구 신내동', 'NON_MATCHED', '1126010400', 37.6100, 127.0951, 1, '분리수거 도와주세요.', NOW() - INTERVAL 24 DAY - INTERVAL 14 HOUR, NOW()),
(1074, 200, '화초 물주기', 'TERM', 2000, 20000, '서울시 동대문구 전농동', 'NON_MATCHED', '1123010400', 37.5735, 127.0384, 0, '주 2회 화분 물주기 부탁해요.', NOW() - INTERVAL 25 DAY, NOW()),
(1075, 100, '전구 교체', 'DAY', 4000, 4000, '서울시 성북구 하월곡동', 'NON_MATCHED', '1129010400', 37.6063, 127.0537, 2, '전구 갈아끼우는 것 도와주세요.', NOW() - INTERVAL 25 DAY - INTERVAL 6 HOUR, NOW()),
(1076, 300, '가전제품 사용법', 'DAY', 5000, 5000, '서울시 강북구 인수동', 'PROCEEDING', '1130510400', 37.6279, 127.0128, 5, '새 가전제품 사용법 알려주세요.', NOW() - INTERVAL 25 DAY - INTERVAL 12 HOUR, NOW()),
(1077, 200, '휴대폰 충전', 'DAY', 2000, 2000, '서울시 도봉구 도봉동', 'NON_MATCHED', '1132010400', 37.6688, 127.0471, 0, '매일 휴대폰 충전 확인 부탁해요.', NOW() - INTERVAL 25 DAY - INTERVAL 18 HOUR, NOW()),
(1078, 100, '약국 대리 방문', 'DAY', 4500, 4500, '서울시 노원구 하계동', 'NON_MATCHED', '1135010400', 37.6381, 127.0662, 1, '약 처방전 가지고 대신 받아와 주세요.', NOW() - INTERVAL 26 DAY, NOW()),
(1079, 300, '공과금 납부', 'DAY', 3500, 3500, '서울시 은평구 갈현동', 'NON_MATCHED', '1138010400', 37.6178, 126.9232, 0, '은행 가서 공과금 납부 대행.', NOW() - INTERVAL 26 DAY - INTERVAL 8 HOUR, NOW()),
(1080, 200, '의류 수선 맡기기', 'DAY', 4000, 4000, '서울시 서대문구 남가좌동', 'PROCEEDING', '1141010400', 37.5790, 126.9161, 3, '수선집에 옷 맡기고 찾아와 주세요.', NOW() - INTERVAL 26 DAY - INTERVAL 16 HOUR, NOW()),

-- Post 81-90: 복합 서비스 (여러 카테고리, 27-29일 전 게시글)
(1081, 100, '종합 돌봄 서비스', 'TERM', 10000, 100000, '서울시 마포구 망원동', 'NON_MATCHED', '1144011300', 37.5555, 126.9049, 2, '외출동행, 가사지원, 식사도움 종합 서비스.', NOW() - INTERVAL 27 DAY, NOW()),
(1082, 300, '오전 케어 서비스', 'DAY', 8000, 8000, '서울시 양천구 염창동', 'NON_MATCHED', '1147510400', 37.5478, 126.8743, 1, '아침 식사 준비와 청소 함께 해주세요.', NOW() - INTERVAL 27 DAY - INTERVAL 7 HOUR, NOW()),
(1083, 200, '오후 돌봄 서비스', 'DAY', 7500, 7500, '서울시 강서구 염창동', 'PROCEEDING', '1150010400', 37.5499, 126.8747, 4, '점심 식사 후 산책 동행과 정리.', NOW() - INTERVAL 27 DAY - INTERVAL 14 HOUR, NOW()),
(1084, 100, '전일 케어', 'DAY', 15000, 15000, '서울시 구로구 개봉동', 'NON_MATCHED', '1153010400', 37.4892, 126.8579, 0, '아침부터 저녁까지 돌봄 서비스.', NOW() - INTERVAL 28 DAY, NOW()),
(1085, 300, '주말 돌봄 서비스', 'DAY', 9000, 9000, '서울시 금천구 시흥동', 'NON_MATCHED', '1154510400', 37.4567, 126.8958, 1, '주말 외출동행과 식사 준비.', NOW() - INTERVAL 28 DAY - INTERVAL 6 HOUR, NOW()),
(1086, 200, '병원 케어 서비스', 'DAY', 12000, 12000, '서울시 영등포구 문래동', 'PROCEEDING', '1156010400', 37.5180, 126.8966, 5, '병원 동행 및 간호 도움.', NOW() - INTERVAL 28 DAY - INTERVAL 12 HOUR, NOW()),
(1087, 100, '재활 지원 서비스', 'TERM', 11000, 110000, '서울시 동작구 사당동', 'NON_MATCHED', '1159010400', 37.4767, 126.9816, 0, '물리치료 동행과 운동 보조.', NOW() - INTERVAL 28 DAY - INTERVAL 18 HOUR, NOW()),
(1088, 300, '통합 가사 서비스', 'DAY', 8500, 8500, '서울시 관악구 인헌동', 'NON_MATCHED', '1162010400', 37.4679, 126.9640, 2, '청소, 빨래, 설거지 통합 서비스.', NOW() - INTERVAL 29 DAY, NOW()),
(1089, 200, '생활 밀착 케어', 'DAY', 9500, 9500, '서울시 강남구 개포동', 'NON_MATCHED', '1168010500', 37.4847, 127.0632, 1, '일상생활 전반 도움 서비스.', NOW() - INTERVAL 29 DAY - INTERVAL 8 HOUR, NOW()),
(1090, 100, '맞춤형 돌봄', 'TERM', 10500, 105000, '서울시 서초구 내곡동', 'PROCEEDING', '1165010900', 37.4605, 127.0811, 3, '개인별 맞춤 돌봄 서비스 제공.', NOW() - INTERVAL 29 DAY - INTERVAL 16 HOUR, NOW()),

-- Post 91-100: 긴급 및 특수 서비스 (30일 전 게시글)
(1091, 300, '긴급 외출 동행', 'DAY', 7000, 7000, '서울시 송파구 삼전동', 'NON_MATCHED', '1171010300', 37.5027, 127.0928, 0, '오늘 긴급하게 병원 동행 필요.', NOW() - INTERVAL 30 DAY, NOW()),
(1092, 200, '야간 간호 서비스', 'DAY', 15000, 15000, '서울시 강동구 성내동', 'PROCEEDING', '1174010400', 37.5300, 127.1248, 4, '밤샘 간호 가능하신 분.', NOW() - INTERVAL 30 DAY - INTERVAL 3 HOUR, NOW()),
(1093, 100, '휴일 돌봄 서비스', 'DAY', 10000, 10000, '서울시 광진구 광장동', 'NON_MATCHED', '1121510500', 37.5463, 127.0983, 1, '공휴일 돌봄 서비스 요청.', NOW() - INTERVAL 30 DAY - INTERVAL 6 HOUR, NOW()),
(1094, 300, '장거리 이동 동행', 'DAY', 12000, 12000, '서울시 성동구 응봉동', 'NON_MATCHED', '1120010500', 37.5491, 127.0283, 0, '타지역 병원 가는 것 동행.', NOW() - INTERVAL 30 DAY - INTERVAL 9 HOUR, NOW()),
(1095, 200, '입원 동행 서비스', 'DAY', 20000, 20000, '서울시 중랑구 중화동', 'NON_MATCHED', '1126010500', 37.5989, 127.0778, 2, '입원 수속 및 초기 케어 도움.', NOW() - INTERVAL 30 DAY - INTERVAL 12 HOUR, NOW()),
(1096, 100, '퇴원 후 케어', 'TERM', 13000, 130000, '서울시 동대문구 용두동', 'PROCEEDING', '1123010500', 37.5751, 127.0404, 5, '퇴원 후 2주간 집중 케어.', NOW() - INTERVAL 30 DAY - INTERVAL 15 HOUR, NOW()),
(1097, 300, '응급 상황 대응', 'DAY', 8000, 8000, '서울시 성북구 안암동', 'NON_MATCHED', '1129010500', 37.5856, 127.0299, 0, '응급 상황 시 연락 가능한 분.', NOW() - INTERVAL 30 DAY - INTERVAL 18 HOUR, NOW()),
(1098, 200, '특별 식단 조리', 'DAY', 9000, 9000, '서울시 강북구 우이동', 'NON_MATCHED', '1130510500', 37.6503, 127.0139, 1, '암 환자 특별 식단 조리.', NOW() - INTERVAL 30 DAY - INTERVAL 20 HOUR, NOW()),
(1099, 100, '전문 간병 서비스', 'TERM', 18000, 180000, '서울시 도봉구 창동', 'NON_MATCHED', '1132010500', 37.6533, 127.0471, 0, '중증 환자 전문 간병.', NOW() - INTERVAL 30 DAY - INTERVAL 22 HOUR, NOW()),
(1100, 300, '재활 운동 코치', 'DAY', 11000, 11000, '서울시 노원구 공릉동', 'PROCEEDING', '1135010500', 37.6254, 127.0739, 3, '뇌졸중 환자 재활 운동 지도.', NOW() - INTERVAL 30 DAY - INTERVAL 23 HOUR, NOW())
ON DUPLICATE KEY UPDATE
    member_id = VALUES(member_id),
    title = VALUES(title),
    type = VALUES(type),
    unit_honey = VALUES(unit_honey),
    total_honey = VALUES(total_honey),
    region = VALUES(region),
    status = VALUES(status),
    legal_dong_code = VALUES(legal_dong_code),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    applicant_count = VALUES(applicant_count),
    content = VALUES(content),
    updated_at = NOW();

-- ========================================
-- PostHelpCategory 더미 데이터
-- ========================================
-- Post와 HelpCategory 매핑 데이터

-- Posts 1-10: 외출동행 (카테고리 1)
INSERT INTO post_help_category (post_id, help_category_id, created_at, updated_at)
VALUES
(1001, 1, NOW(), NOW()),
(1002, 1, NOW(), NOW()),
(1003, 1, NOW(), NOW()),
(1004, 1, NOW(), NOW()),
(1005, 1, NOW(), NOW()),
(1006, 1, NOW(), NOW()),
(1007, 1, NOW(), NOW()),
(1008, 1, NOW(), NOW()),
(1009, 1, NOW(), NOW()),
(1010, 1, NOW(), NOW()),

-- Posts 11-20: 방문목욕 (카테고리 2)
(1011, 2, NOW(), NOW()),
(1012, 2, NOW(), NOW()),
(1013, 2, NOW(), NOW()),
(1014, 2, NOW(), NOW()),
(1015, 2, NOW(), NOW()),
(1016, 2, NOW(), NOW()),
(1017, 2, NOW(), NOW()),
(1018, 2, NOW(), NOW()),
(1019, 2, NOW(), NOW()),
(1020, 2, NOW(), NOW()),

-- Posts 21-30: 방문간호 (카테고리 3)
(1021, 3, NOW(), NOW()),
(1022, 3, NOW(), NOW()),
(1023, 3, NOW(), NOW()),
(1024, 3, NOW(), NOW()),
(1025, 3, NOW(), NOW()),
(1026, 3, NOW(), NOW()),
(1027, 3, NOW(), NOW()),
(1028, 3, NOW(), NOW()),
(1029, 3, NOW(), NOW()),
(1030, 3, NOW(), NOW()),

-- Posts 31-40: 가사지원 (카테고리 4)
(1031, 4, NOW(), NOW()),
(1032, 4, NOW(), NOW()),
(1033, 4, NOW(), NOW()),
(1034, 4, NOW(), NOW()),
(1035, 4, NOW(), NOW()),
(1036, 4, NOW(), NOW()),
(1037, 4, NOW(), NOW()),
(1038, 4, NOW(), NOW()),
(1039, 4, NOW(), NOW()),
(1040, 4, NOW(), NOW()),

-- Posts 41-50: 식사도움 (카테고리 5)
(1041, 5, NOW(), NOW()),
(1042, 5, NOW(), NOW()),
(1043, 5, NOW(), NOW()),
(1044, 5, NOW(), NOW()),
(1045, 5, NOW(), NOW()),
(1046, 5, NOW(), NOW()),
(1047, 5, NOW(), NOW()),
(1048, 5, NOW(), NOW()),
(1049, 5, NOW(), NOW()),
(1050, 5, NOW(), NOW()),

-- Posts 51-60: 학습지원 (카테고리 6)
(1051, 6, NOW(), NOW()),
(1052, 6, NOW(), NOW()),
(1053, 6, NOW(), NOW()),
(1054, 6, NOW(), NOW()),
(1055, 6, NOW(), NOW()),
(1056, 6, NOW(), NOW()),
(1057, 6, NOW(), NOW()),
(1058, 6, NOW(), NOW()),
(1059, 6, NOW(), NOW()),
(1060, 6, NOW(), NOW()),

-- Posts 61-70: 정서적 지원 (카테고리 7)
(1061, 7, NOW(), NOW()),
(1062, 7, NOW(), NOW()),
(1063, 7, NOW(), NOW()),
(1064, 7, NOW(), NOW()),
(1065, 7, NOW(), NOW()),
(1066, 7, NOW(), NOW()),
(1067, 7, NOW(), NOW()),
(1068, 7, NOW(), NOW()),
(1069, 7, NOW(), NOW()),
(1070, 7, NOW(), NOW()),

-- Posts 71-80: 기타생활지원 (카테고리 8)
(1071, 8, NOW(), NOW()),
(1072, 8, NOW(), NOW()),
(1073, 8, NOW(), NOW()),
(1074, 8, NOW(), NOW()),
(1075, 8, NOW(), NOW()),
(1076, 8, NOW(), NOW()),
(1077, 8, NOW(), NOW()),
(1078, 8, NOW(), NOW()),
(1079, 8, NOW(), NOW()),
(1080, 8, NOW(), NOW()),

-- Posts 81-90: 복합 서비스 (여러 카테고리)
(1081, 1, NOW(), NOW()),
(1081, 4, NOW(), NOW()),
(1081, 5, NOW(), NOW()),
(1082, 4, NOW(), NOW()),
(1082, 5, NOW(), NOW()),
(1083, 1, NOW(), NOW()),
(1083, 4, NOW(), NOW()),
(1084, 1, NOW(), NOW()),
(1084, 4, NOW(), NOW()),
(1084, 5, NOW(), NOW()),
(1085, 1, NOW(), NOW()),
(1085, 5, NOW(), NOW()),
(1086, 1, NOW(), NOW()),
(1086, 3, NOW(), NOW()),
(1087, 1, NOW(), NOW()),
(1087, 3, NOW(), NOW()),
(1088, 4, NOW(), NOW()),
(1089, 4, NOW(), NOW()),
(1089, 5, NOW(), NOW()),
(1090, 1, NOW(), NOW()),
(1090, 4, NOW(), NOW()),
(1090, 5, NOW(), NOW()),

-- Posts 91-100: 긴급 및 특수 서비스
(1091, 1, NOW(), NOW()),
(1092, 3, NOW(), NOW()),
(1093, 4, NOW(), NOW()),
(1093, 5, NOW(), NOW()),
(1094, 1, NOW(), NOW()),
(1095, 3, NOW(), NOW()),
(1096, 3, NOW(), NOW()),
(1097, 3, NOW(), NOW()),
(1098, 5, NOW(), NOW()),
(1099, 3, NOW(), NOW()),
(1100, 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    updated_at = NOW();

-- ========================================
-- PostPeriod 및 PostSchedule 더미 데이터는 V4 마이그레이션 파일에 있음
-- V6__Insert_Post_Period_And_Schedule_Dummy_Data.sql 참조
-- ========================================

-- ========================================
-- PostImage 더미 데이터
-- ========================================
-- 일부 게시글에 대한 이미지 정보

INSERT INTO post_image (image_id, post_id, image_url, sequence, created_at, updated_at)
VALUES
(3001, 1001, 'https://example.com/posts/1001/img1.jpg', 1, NOW(), NOW()),
(3002, 1003, 'https://example.com/posts/1003/img1.jpg', 1, NOW(), NOW()),
(3003, 1006, 'https://example.com/posts/1006/img1.jpg', 1, NOW(), NOW()),
(3004, 1006, 'https://example.com/posts/1006/img2.jpg', 2, NOW(), NOW()),
(3005, 1013, 'https://example.com/posts/1013/img1.jpg', 1, NOW(), NOW()),
(3006, 1019, 'https://example.com/posts/1019/img1.jpg', 1, NOW(), NOW()),
(3007, 1024, 'https://example.com/posts/1024/img1.jpg', 1, NOW(), NOW()),
(3008, 1029, 'https://example.com/posts/1029/img1.jpg', 1, NOW(), NOW()),
(3009, 1034, 'https://example.com/posts/1034/img1.jpg', 1, NOW(), NOW()),
(3010, 1034, 'https://example.com/posts/1034/img2.jpg', 2, NOW(), NOW()),
(3011, 1040, 'https://example.com/posts/1040/img1.jpg', 1, NOW(), NOW()),
(3012, 1044, 'https://example.com/posts/1044/img1.jpg', 1, NOW(), NOW()),
(3013, 1049, 'https://example.com/posts/1049/img1.jpg', 1, NOW(), NOW()),
(3014, 1053, 'https://example.com/posts/1053/img1.jpg', 1, NOW(), NOW()),
(3015, 1058, 'https://example.com/posts/1058/img1.jpg', 1, NOW(), NOW()),
(3016, 1062, 'https://example.com/posts/1062/img1.jpg', 1, NOW(), NOW()),
(3017, 1067, 'https://example.com/posts/1067/img1.jpg', 1, NOW(), NOW()),
(3018, 1067, 'https://example.com/posts/1067/img2.jpg', 2, NOW(), NOW()),
(3019, 1072, 'https://example.com/posts/1072/img1.jpg', 1, NOW(), NOW()),
(3020, 1076, 'https://example.com/posts/1076/img1.jpg', 1, NOW(), NOW()),
(3021, 1080, 'https://example.com/posts/1080/img1.jpg', 1, NOW(), NOW()),
(3022, 1083, 'https://example.com/posts/1083/img1.jpg', 1, NOW(), NOW()),
(3023, 1086, 'https://example.com/posts/1086/img1.jpg', 1, NOW(), NOW()),
(3024, 1086, 'https://example.com/posts/1086/img2.jpg', 2, NOW(), NOW()),
(3025, 1090, 'https://example.com/posts/1090/img1.jpg', 1, NOW(), NOW()),
(3026, 1092, 'https://example.com/posts/1092/img1.jpg', 1, NOW(), NOW()),
(3027, 1096, 'https://example.com/posts/1096/img1.jpg', 1, NOW(), NOW()),
(3028, 1096, 'https://example.com/posts/1096/img2.jpg', 2, NOW(), NOW()),
(3029, 1100, 'https://example.com/posts/1100/img1.jpg', 1, NOW(), NOW()),
(3030, 1100, 'https://example.com/posts/1100/img2.jpg', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    image_url = VALUES(image_url),
    sequence = VALUES(sequence),
    updated_at = NOW();