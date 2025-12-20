package com.lgcns.bebee.member.domain.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.lgcns.bebee.member.application.client.OcrClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

/**
 * 문서 위변조 검증 서비스
 * 
 * 1차 버전: 룰 기반으로 구현
 * 향후: Python/AI 모듈로 교체 가능한 인터페이스 구조
 */
@Service
public class DocumentVerificationService {

    private final OcrClient ocrClient;

    public DocumentVerificationService(OcrClient ocrClient) {
        this.ocrClient = ocrClient;
    }

    /**
     * 업로드된 파일을 분석하고, 위변조 관련 점수와 플래그를 계산
     * @param file 분석할 파일
     * @return 분석 결과
     */
    public AnalysisResult analyze(MultipartFile file) {
        int baseScore = calcBaseScore(file);
        int exifScore = calcExifScore(file);
        int ocrScore = calcOcrScore(file);
        int forgeryScore = calcForgeryScore(baseScore, exifScore, ocrScore);
        String systemFlag = decideSystemFlag(forgeryScore);

        return new AnalysisResult(exifScore, ocrScore, forgeryScore, systemFlag);
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
     * EXIF 메타데이터 기반 점수 계산
     * - EXIF 데이터 존재 여부
     * - 촬영 날짜 유무 및 합리성
     * - 이미지 편집 소프트웨어 흔적
     * - 카메라 정보 유무
     */
    private int calcExifScore(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            return evaluateExifMetadata(metadata);
        } catch (Exception e) {
            // EXIF 읽기 실패 = 스크린샷/편집 이미지 가능성
            return 30;
        }
    }

    /**
     * EXIF 메타데이터 평가
     */
    private int evaluateExifMetadata(Metadata metadata) {
        int score = 100;

        ExifSubIFDDirectory subIFD = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        // 1. EXIF 데이터 존재 여부
        if (subIFD == null && ifd0 == null) {
            // EXIF 데이터 없음 → 스크린샷/다운로드 이미지 의심
            return 30;
        }

        // 2. 촬영 날짜 확인
        if (subIFD != null) {
            Date originalDate = subIFD.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            if (originalDate == null) {
                score -= 20; // 촬영 날짜 없음
            } else {
                // 너무 오래된 사진 (1년 이상)
                long daysDiff = (System.currentTimeMillis() - originalDate.getTime()) / (1000L * 60 * 60 * 24);
                if (daysDiff > 365) {
                    score -= 15;
                }
            }
        } else {
            score -= 20;
        }

        // 3. 소프트웨어 정보 확인 (편집 프로그램 흔적)
        if (ifd0 != null) {
            String software = ifd0.getString(ExifIFD0Directory.TAG_SOFTWARE);
            if (software != null) {
                String lower = software.toLowerCase();
                if (lower.contains("photoshop") || lower.contains("gimp") ||
                        lower.contains("paint") || lower.contains("editor") ||
                        lower.contains("lightroom")) {
                    score -= 30; // 이미지 편집 소프트웨어 사용 흔적
                }
            }

            // 4. 카메라 제조사/모델 확인
            String make = ifd0.getString(ExifIFD0Directory.TAG_MAKE);
            String model = ifd0.getString(ExifIFD0Directory.TAG_MODEL);
            if (make == null && model == null) {
                score -= 10; // 카메라 정보 없음
            }
        }

        return clamp(score);
    }

    /**
     * OCR 텍스트 인식 기반 점수 계산
     * - 외부 OCR 클라이언트의 신뢰도(confidence)를 0~100 점수로 변환
     * - 키워드가 존재하지 않으면 소폭 감점
     */
    private int calcOcrScore(MultipartFile file) {
        if (ocrClient == null) {
            return 75;
        }

        OcrClient.OcrResult result = ocrClient.analyze(file);
        if (result == null || result.confidence() == null) {
            return 50;
        }

        int score = (int) Math.round(result.confidence() * 100);
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
     * @param score 종합 점수
     * @return LOW(의심 낮음) / MID(중간) / HIGH(의심 높음)
     */
    private String decideSystemFlag(int score) {
        if (score >= 80) return "LOW";   // 위변조 의심 거의 없음
        if (score >= 50) return "MID";   // 중간
        return "HIGH";                    // 의심 높음
    }

    /**
     * 점수를 0~100 범위로 제한
     */
    private int clamp(int val) {
        return Math.max(0, Math.min(100, val));
    }

    /**
     * 분석 결과 DTO
     * @param exifScore EXIF 점수 (0-100)
     * @param ocrScore OCR 점수 (0-100)
     * @param forgeryScore 종합 위변조 점수 (0-100)
     * @param systemFlag 시스템 플래그 (LOW/MID/HIGH)
     */
    public record AnalysisResult(
            Integer exifScore,
            Integer ocrScore,
            Integer forgeryScore,
            String systemFlag
    ) {}
}

