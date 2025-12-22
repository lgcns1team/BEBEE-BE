package com.lgcns.bebee.member.infrastructure.ocr;

import com.lgcns.bebee.member.application.client.OcrClient;
import com.lgcns.bebee.member.common.exception.DocumentErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * HTTP 기반 OCR 클라이언트 구현체
 * Python OCR 서비스와 통신
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpOcrClientImpl implements OcrClient {
    
    private final WebClient ocrWebClient;
    
    /**
     * 이미지 파일에서 텍스트를 추출하고 분석
     * 
     * @param file 분석할 이미지 파일
     * @return OCR 분석 결과
     */
    @Override
    public OcrResult analyze(MultipartFile file) {
        try {
            log.debug("OCR 분석 요청: {}", file.getOriginalFilename());
            
            // MultipartFile을 Resource로 변환하여 MultiValueMap에 추가
            Resource resource = file.getResource();
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file", resource);
            
            // OCR 서비스 호출
            OcrResponse response = ocrWebClient.post()
                .uri("/api/ocr/analyze")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .retrieve()
                .bodyToMono(OcrResponse.class)
                .block();
            
            if (response == null) {
                throw DocumentErrors.OCR_FAILED.toException();
            }
            
            log.debug("OCR 분석 완료: confidence={}, keywords={}, names={}", 
                     response.getConfidence(), response.getKeywords(), response.getNames());
            
            return new OcrResult(
                response.getExtractedText(),
                response.getConfidence(),
                response.getKeywords(),
                response.getNames()
            );
            
        } catch (WebClientResponseException e) {
            log.error("OCR 서비스 호출 실패: status={}, body={}", 
                     e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw DocumentErrors.OCR_FAILED.toException();
        } catch (Exception e) {
            log.error("OCR 분석 중 예외 발생", e);
            throw DocumentErrors.OCR_FAILED.toException();
        }
    }
    
    /**
     * OCR 서비스 응답 DTO
     */
    @lombok.Getter
    @lombok.Setter
    private static class OcrResponse {
        private String extractedText;
        private Double confidence;
        private Integer ocrScore;
        private List<String> keywords;
        private List<String> names;
    }
}
