package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 문서 검증 Repository
 */
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {

    /**
     * 상태별 문서 검증 목록 조회
     * @param status 문서 상태 (PENDING, APPROVED, REJECTED)
     * @return 해당 상태의 문서 검증 목록
     */
    List<DocumentVerification> findByStatus(DocumentStatus status);

    /**
     * 특정 회원의 문서 검증 목록 조회
     * @param memberId 회원 ID
     * @return 해당 회원의 문서 검증 목록
     */
    List<DocumentVerification> findByDocumentMemberMemberId(Long memberId);

    /**
     * 특정 문서의 검증 정보 조회
     * @param documentId 문서 ID
     * @return 해당 문서의 검증 목록
     */
    List<DocumentVerification> findByDocumentDocumentId(Long documentId);
}
