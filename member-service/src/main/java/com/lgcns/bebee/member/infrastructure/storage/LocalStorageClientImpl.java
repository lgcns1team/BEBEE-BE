package com.lgcns.bebee.member.infrastructure.storage;

import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.common.exception.DocumentErrors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 로컬 파일 저장 구현체
 * 개발/테스트 환경용 (운영 시 S3로 교체)
 */
@Component
public class LocalStorageClientImpl implements FileStorageClient {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080/files}")
    private String baseUrl;

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @param directory 저장 디렉토리
     * @return 업로드된 파일의 URL
     */
    @Override
    public String upload(MultipartFile file, String directory) {
        try {
            // 저장 디렉토리 생성
            Path uploadPath = Paths.get(uploadDir, directory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일명 생성 (UUID + 원본 확장자)
            String originalFilename = file.getOriginalFilename();
            String extension = getExtension(originalFilename);
            String newFilename = UUID.randomUUID() + extension;

            // 파일 저장
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // URL 반환
            return baseUrl + "/" + directory + "/" + newFilename;

        } catch (IOException e) {
            throw DocumentErrors.FILE_UPLOAD_FAILED.toException();
        }
    }

    /**
     * 파일 삭제
     * @param fileUrl 삭제할 파일 URL
     */
    @Override
    public void delete(String fileUrl) {
        try {
            // URL에서 파일 경로 추출
            String relativePath = fileUrl.replace(baseUrl + "/", "");
            Path filePath = Paths.get(uploadDir, relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            // 삭제 실패는 로그만 남기고 무시 (best effort)
        }
    }

    /**
     * 파일 확장자 추출
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}

