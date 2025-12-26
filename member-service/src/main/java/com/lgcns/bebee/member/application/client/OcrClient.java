package com.lgcns.bebee.member.application.client;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * OCR 서비스 클라이언트 인터페이스
 * 외부 OCR 서비스를 추상화
 */
public interface OcrClient {

    /**
     * 이미지 파일에서 텍스트를 추출하고 분석
     * 
     * @param file 분석할 이미지 파일
     * @param role 사용자 역할 (HELPER 또는 DISABLED, null 가능)
     * @return OCR 분석 결과
     */
    OcrResult analyze(MultipartFile file, String role);

    /**
     * OCR 분석 결과
     *
     * @param extractedText 추출된 텍스트
     * @param confidence    신뢰도 (0.0 ~ 1.0)
     * @param keywords      발견된 키워드 목록
     * @param names         추출된 이름 후보
     */
    record OcrResult(
            String extractedText,
            Double confidence,
            List<String> keywords,
            List<String> names,
            java.util.Map<String, String> fields) {
    }
}
