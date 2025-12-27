package com.lgcns.bebee.file.infrastructure;

import com.lgcns.bebee.file.application.client.PreSignedUrlClient;
import com.lgcns.bebee.file.core.S3Properties;
import com.lgcns.bebee.file.domain.PresignedUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class S3PresignedUrlGenerator implements PreSignedUrlClient {
    private final S3Presigner s3Presigner;
    private final S3Properties s3Properties;

    public PresignedUrl generatePresignedUrl(String directory, String entityId, String originFileName, String contentType) {
        String objectKey = buildObjectKey(directory, entityId, originFileName);

        // PutObject 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getS3().getBucket())
                .key(objectKey)
                .contentType(contentType)
                .build();

        Duration duration = Duration.ofMinutes(s3Properties.getS3().getPresignedUrlExpirationMinutes());

        // Presigned URL 요청 생성
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putObjectRequest)
                .build();

        // Presigned URL 생성
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        // 업로드용 URL
        String uploadUrl = presignedRequest.url().toString();

        // 최종 파일 접근 URL 생성 (환경에 따라 다름)
        String fileUrl = generateFileUrl(objectKey);

        return new PresignedUrl(
                uploadUrl,
                fileUrl,
                duration.getSeconds()
        );
    }

    private String buildObjectKey(String directory, String entityId, String originFileName) {
        return directory + "/" + buildFileName(entityId, originFileName);
    }

    private String buildFileName(String entityId, String originFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String extension = extractExtension(originFileName);

        return String.format("%s_%s.%s", timestamp, entityId, extension);
    }

    private String extractExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 환경에 따른 파일 접근 URL 생성
     * - base-url이 설정되어 있으면: CloudFront 등 커스텀 URL 사용 (운영 환경)
     * - base-url이 없으면: S3 직접 URL 사용 (로컬, 개발 환경)
     */
    private String generateFileUrl(String objectKey) {
        String baseUrl = s3Properties.getS3().getBaseUrl();

        if (baseUrl != null && !baseUrl.isEmpty()) {
            // CloudFront 등 커스텀 베이스 URL 사용 (운영 환경)
            return baseUrl + "/" + objectKey;
        } else {
            // S3 직접 URL 사용 (개발 환경)
            String bucket = s3Properties.getS3().getBucket();
            String region = s3Properties.getRegion().getStatic();

            return String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucket,
                    region,
                    objectKey
            );
        }
    }
}