package com.lgcns.bebee.member.infrastructure.ocr;

import com.lgcns.bebee.member.application.client.OcrClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * OCR 서비스 HTTP 클라이언트 구현체
 * 외부 OCR 서비스와 HTTP 통신하여 문서 분석 수행
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpOcrClient implements OcrClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ocr.service.url:http://localhost:8086}")
    private String ocrServiceUrl;

    /**
     * OCR 서비스를 호출하여 문서 분석
     *
     * @param file 분석할 파일
     * @param role 사용자 역할 (HELPER 또는 DISABLED)
     * @return OCR 분석 결과
     */
    @Override
    public OcrResult analyze(MultipartFile file, String role) {
        try {
            // Multipart 요청 생성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            if (role != null) {
                body.add("role", role);
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            log.info("OCR 서비스 호출 시작: URL={}, role={}", ocrServiceUrl + "/api/ocr/analyze", role);
            // OCR 서비스 호출
            String url = ocrServiceUrl + "/api/ocr/analyze";
            ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, requestEntity,
                    (Class<Map<String, Object>>) (Class<?>) Map.class);

            log.info("OCR 서비스 응답 수신: status={}", response.getStatusCode());

            // 응답 파싱
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                return new OcrResult("", 0.0, List.of(), List.of(), Map.of());
            }

            String extractedText = (String) responseBody.getOrDefault("extractedText", "");
            Double confidence = ((Number) responseBody.getOrDefault("confidence", 0.0)).doubleValue();

            @SuppressWarnings("unchecked")
            List<String> keywords = (List<String>) responseBody.getOrDefault("keywords", List.of());

            @SuppressWarnings("unchecked")
            List<String> names = (List<String>) responseBody.getOrDefault("names", List.of());

            @SuppressWarnings("unchecked")
            Map<String, String> fields = (Map<String, String>) responseBody.getOrDefault("fields", Map.of());

            return new OcrResult(extractedText, confidence, keywords, names, fields);

        } catch (IOException e) {
            throw new RuntimeException("OCR 서비스 호출 중 파일 읽기 실패", e);
        } catch (Exception e) {
            throw new RuntimeException("OCR 서비스 호출 실패: " + e.getMessage(), e);
        }
    }
}
