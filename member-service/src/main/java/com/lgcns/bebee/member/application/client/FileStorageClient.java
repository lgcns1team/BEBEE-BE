package com.lgcns.bebee.member.application.client;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 저장소 클라이언트 인터페이스
 * 파일 업로드 및 관리
 */
public interface FileStorageClient {

    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @param directory 저장 디렉토리
     * @return 업로드된 파일의 URL
     */
    String upload(MultipartFile file, String directory);

    /**
     * 파일 삭제
     * @param fileUrl 삭제할 파일 URL
     */
    void delete(String fileUrl);
}
