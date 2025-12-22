package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.core.exception.DocumentErrors;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import com.lgcns.bebee.member.domain.repository.DocumentRepository;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 문서 검증 도메인 서비스
 * 문서 검증 관련 핵심 비즈니스 로직을 담당
 */
@Service
@RequiredArgsConstructor
public class DocumentManagement {

    private final DocumentRepository documentRepository;
    private final DocumentVerificationRepository documentVerificationRepository;

    /**
     * Document 단건 조회
     * @param documentId 문서 ID
     * @return Document 엔티티
     * @throws RuntimeException 문서를 찾을 수 없는 경우
     */
    public Document loadDocument(@NonNull Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(DocumentErrors.DOCUMENT_NOT_FOUND::toException);
    }

    /**
     * 문서 검증 단건 조회
     * @param verificationId 검증 ID
     * @return DocumentVerification 엔티티
     * @throws RuntimeException 문서를 찾을 수 없는 경우
     */
    public DocumentVerification load(@NonNull Long verificationId) {
        return documentVerificationRepository.findById(verificationId)
                .orElseThrow(DocumentErrors.NOT_FOUND::toException);
    }

    /**
     * PENDING 상태의 문서 검증 목록 조회
     * @return PENDING 상태의 문서 검증 목록
     */
    public List<DocumentVerification> loadPendingList() {
        return documentVerificationRepository.findByStatus(DocumentStatus.PENDING);
    }

    /**
     * 문서 검증 승인 처리
     * 비즈니스 규칙: PENDING 상태인 경우에만 승인 가능
     * @param verification 검증 엔티티
     * @throws RuntimeException 이미 처리된 문서인 경우
     */
    public void approve(DocumentVerification verification) {
        validatePendingStatus(verification);
        verification.approve();
    }

    /**
     * 문서 검증 거절 처리
     * 비즈니스 규칙: PENDING 상태인 경우에만 거절 가능
     * @param verification 검증 엔티티
     * @param reason 거절 사유
     * @throws RuntimeException 이미 처리된 문서인 경우
     */
    public void reject(DocumentVerification verification, String reason) {
        validatePendingStatus(verification);
        verification.reject(reason);
    }

    /**
     * PENDING 상태 검증
     * @param verification 검증 엔티티
     * @throws RuntimeException PENDING 상태가 아닌 경우
     */
    private void validatePendingStatus(DocumentVerification verification) {
        if (verification.getStatus() != DocumentStatus.PENDING) {
            throw DocumentErrors.ALREADY_PROCESSED.toException();
        }
    }
}
