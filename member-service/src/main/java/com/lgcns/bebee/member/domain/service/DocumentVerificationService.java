package com.lgcns.bebee.member.domain.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.lgcns.bebee.member.application.client.OcrClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 문서 검증 서비스
 * 업로드된 문서의 위변조 여부를 분석하고 점수를 산출
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVerificationService {

    private final OcrClient ocrClient;

    /**
     * S3 URL의 이미지 파일을 분석 (신규 방식)
     * 
     * @param fileUrl 분석할 S3 파일 URL
     * @param role    사용자 역할
     * @return 분석 결과
     */
    public AnalysisResult analyze(String fileUrl, String role) {
        log.info("문서 URL 분석 시작: {}", maskUrl(fileUrl));

        int baseScore = calcBaseScoreFromUrl(fileUrl);
        int exifScore = calcExifScore(fileUrl);
        int ocrScore = calcOcrScore(fileUrl, role);
        int forgeryScore = calcForgeryScore(baseScore, exifScore, ocrScore);
        String systemFlag = decideSystemFlag(forgeryScore);

        return new AnalysisResult(exifScore, ocrScore, forgeryScore, systemFlag);
    }

    /**
     * 업로드된 파일을 직접 분석 (레거시 방식 - 파일 전송)
     */
    public AnalysisResult analyze(MultipartFile file, String role) {
        int baseScore = calcBaseScore(file);
        int exifScore = calcExifScore(file);
        int ocrScore = calcOcrScore(file, role);
        int forgeryScore = calcForgeryScore(baseScore, exifScore, ocrScore);
        String systemFlag = decideSystemFlag(forgeryScore);

        return new AnalysisResult(exifScore, ocrScore, forgeryScore, systemFlag);
    }

    /**
     * OCR 원본 데이터 추출
     */
    public OcrClient.OcrResult extractRawOcr(MultipartFile file, String role) {
        return ocrClient.analyze(file, role);
    }

    /**
     * 파일 크기, 확장자 등 기본 점수 계산
     */
    private int calcBaseScore(MultipartFile file) {
        int score = 100;
        long size = file.getSize();

        // 10KB 미만이면 지나치게 작은 이미지로 보고 감점
        if (size < 10 * 1024) {
            score -= 20;
        }

        String originalName = file.getOriginalFilename();
        if (originalName != null) {
            String lower = originalName.toLowerCase();
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                    || lower.endsWith(".png") || lower.endsWith(".pdf"))) {
                // 허용하지 않는 확장자면 강한 감점
                score -= 40;
            }
        }

        return clamp(score);
    }

    /**
     * URL에서 확장자 검증하여 기본 점수 계산
     */
    private int calcBaseScoreFromUrl(String fileUrl) {
        int score = 100;

        // URL에서 확장자 검증
        String lower = fileUrl.toLowerCase();
        // 쿼리스트링 제거 후 확장자 확인
        int queryIndex = lower.indexOf('?');
        String pathPart = queryIndex > 0 ? lower.substring(0, queryIndex) : lower;

        if (!(pathPart.endsWith(".jpg") || pathPart.endsWith(".jpeg")
                || pathPart.endsWith(".png") || pathPart.endsWith(".pdf"))) {
            score -= 40;
        }

        return clamp(score);
    }

    /**
     * S3 URL 유효성 검증 (SSRF 방지)
     */
    private boolean isValidS3Url(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }
        // S3 버킷 URL 또는 CloudFront URL 패턴만 허용
        return url.matches("^https://[\\w.-]+\\.s3\\.[\\w-]+\\.amazonaws\\.com/.*$")
                || url.matches("^https://[\\w.-]+\\.cloudfront\\.net/.*$")
                || url.matches("^https://images\\.be-bee\\.link/.*$");
    }

    /**
     * URL 마스킹 (민감 정보 로깅 방지)
     */
    private String maskUrl(String url) {
        if (url == null) return null;
        int queryIndex = url.indexOf('?');
        return queryIndex > 0 ? url.substring(0, queryIndex) + "?[MASKED]" : url;
    }

    /**
     * EXIF 메타데이터 기반 점수 계산 (URL 방식)
     */
    private int calcExifScore(String fileUrl) {
        // SSRF 방지: S3 URL 패턴만 허용
        if (!isValidS3Url(fileUrl)) {
            log.warn("유효하지 않은 S3 URL: {}", maskUrl(fileUrl));
            return 70;  // EXIF 실패 시 기본값 상향 (OCR 실패 시에도 MID 보장)
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URI(fileUrl).toURL().openConnection();
            conn.setInstanceFollowRedirects(false);  // SSRF 방지: 리다이렉트 비활성화
            conn.setConnectTimeout(5000);  // 연결 타임아웃 5초
            conn.setReadTimeout(10000);    // 읽기 타임아웃 10초
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.warn("S3 파일 접근 실패, 응답 코드: {}", responseCode);
                return 70;  // EXIF 실패 시 기본값 상향
            }

            try (InputStream is = conn.getInputStream()) {
                return extractExifScore(is);
            }
        } catch (Exception e) {
            log.error("URL 기반 EXIF 분석 중 오류 발생: {}", e.getMessage());
            return 70;  // EXIF 실패 시 기본값 상향
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * EXIF 메타데이터 기반 점수 계산 (파일 방식)
     */
    private int calcExifScore(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return extractExifScore(is);
        } catch (Exception e) {
            log.error("파일 기반 EXIF 분석 중 오류 발생: {}", e.getMessage());
            return 70;  // EXIF 실패 시 기본값 상향
        }
    }

    private int extractExifScore(InputStream is) {
        int score = 0;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null) {
                if (directory.containsTag(ExifIFD0Directory.TAG_MAKE) ||
                        directory.containsTag(ExifIFD0Directory.TAG_MODEL)) {
                    score += 60;
                }

                String software = directory.getString(ExifIFD0Directory.TAG_SOFTWARE);
                if (software != null) {
                    String lower = software.toLowerCase();
                    if (lower.contains("adobe") || lower.contains("photoshop") || lower.contains("edit")) {
                        score -= 30;
                    } else {
                        score += 20;
                    }
                }
                score += 20;
                
                // EXIF 메타데이터가 있어도 유효한 태그가 없으면 최소 70점 보장
                if (score < 70) {
                    log.warn("EXIF 메타데이터는 있으나 유효한 태그가 부족합니다. 기본값 70점 적용");
                    score = 70;
                }
            } else {
                log.warn("파일에 EXIF 메타데이터가 없습니다.");
                score = 70;  // EXIF 없을 때 기본값 상향
            }
        } catch (Exception e) {
            log.error("EXIF 추출 중 오류: {}", e.getMessage());
            return 70;  // EXIF 실패 시 기본값 상향
        }
        return clamp(score);
    }

    /**
     * OCR 텍스트 인식 기반 점수 계산 (URL 방식)
     */
    private int calcOcrScore(String fileUrl, String role) {
        // SSRF 방지: S3 URL 패턴만 허용
        if (!isValidS3Url(fileUrl)) {
            log.warn("OCR 분석 - 유효하지 않은 S3 URL: {}", maskUrl(fileUrl));
            return 50;
        }

        try {
            OcrClient.OcrResult result = ocrClient.extract(fileUrl, role);
            return processOcrResult(result);
        } catch (Exception e) {
            log.warn("OCR 분석 실패 (기본값 50점 적용): {}", e.getMessage());
            return 50;  // OCR 실패 시 기본 점수 반환
        }
    }

    /**
     * OCR 텍스트 인식 기반 점수 계산 (파일 방식)
     */
    private int calcOcrScore(MultipartFile file, String role) {
        try {
            OcrClient.OcrResult result = ocrClient.analyze(file, role);
            return processOcrResult(result);
        } catch (Exception e) {
            log.warn("OCR 분석 실패 (기본값 50점 적용): {}", e.getMessage());
            return 50;  // OCR 실패 시 기본 점수 반환
        }
    }

    private int processOcrResult(OcrClient.OcrResult result) {
        if (result == null || result.confidence() == null) {
            return 50;
        }

        int score = (int) Math.round(result.confidence() * 100);

        // 신분증 관련 키워드가 전혀 없으면 감점
        if (result.keywords() == null || result.keywords().isEmpty()) {
            score -= 10;
        }

        return clamp(score);
    }

    /**
     * 종합 위변조 점수 계산
     * 가중치: base 30% + exif 30% + ocr 40%
     */
    private int calcForgeryScore(int base, int exif, int ocr) {
        double result = base * 0.3 + exif * 0.3 + ocr * 0.4;
        return clamp((int) Math.round(result));
    }

    /**
     * 시스템 플래그 결정
     * 
     * @param score 종합 점수
     * @return LOW(의심 낮음) / MID(중간) / HIGH(의심 높음)
     */
    private String decideSystemFlag(int score) {
        if (score >= 80)
            return "LOW"; // 위변조 의심 낮음 (자동 통과) - 기준 완화: 90 -> 80
        if (score >= 60)
            return "MID"; // 중간 (관리자 검토 필요)
        return "HIGH"; // 의심 높음 (가입 차단)
    }

    /**
     * 점수를 0~100 범위로 제한
     */
    private int clamp(int val) {
        return Math.max(0, Math.min(100, val));
    }

    /**
     * 분석 결과 DTO
     * 
     * @param exifScore    EXIF 점수 (0-100)
     * @param ocrScore     OCR 점수 (0-100)
     * @param forgeryScore 종합 위변조 점수 (0-100)
     * @param systemFlag   시스템 플래그 (LOW/MID/HIGH)
     */
    public record AnalysisResult(
            Integer exifScore,
            Integer ocrScore,
            Integer forgeryScore,
            String systemFlag) {
    }
}
