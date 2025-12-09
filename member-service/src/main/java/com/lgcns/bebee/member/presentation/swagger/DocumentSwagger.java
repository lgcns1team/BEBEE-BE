package com.lgcns.bebee.member.presentation.swagger;

import com.lgcns.bebee.member.presentation.dto.DocumentRejectReqDTO;
import com.lgcns.bebee.member.presentation.dto.DocumentUploadResDTO;
import com.lgcns.bebee.member.presentation.dto.DocumentVerificationResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 문서 검증 API Swagger 인터페이스
 * Controller가 이 인터페이스를 구현하여 API 문서화
 * TODO: Swagger 의존성 추가 후 @Tag, @Operation 어노테이션 적용
 */
public interface DocumentSwagger {

    /**
     * 문서 업로드
     */
    ResponseEntity<DocumentUploadResDTO> uploadDocument(
            Long memberId,
            Long documentId,
            MultipartFile file
    );

    /**
     * 문서 검증 정보 조회
     */
    ResponseEntity<DocumentVerificationResDTO> getVerification(Long verificationId);

    /**
     * PENDING 문서 목록 조회 (관리자용)
     */
    ResponseEntity<List<DocumentVerificationResDTO>> getPendingList();

    /**
     * 문서 승인 (관리자용)
     */
    ResponseEntity<Void> approveDocument(Long verificationId);

    /**
     * 문서 거절 (관리자용)
     */
    ResponseEntity<Void> rejectDocument(Long verificationId, DocumentRejectReqDTO request);
}
