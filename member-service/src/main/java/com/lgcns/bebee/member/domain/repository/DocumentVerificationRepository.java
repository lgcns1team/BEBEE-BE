package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 문서 검증 Repository
 */
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {

    List<DocumentVerification> findByStatus(DocumentStatus status);

    @Query("SELECT dv FROM DocumentVerification dv WHERE dv.document.memberId = :memberId")
    List<DocumentVerification> findByMemberId(Long memberId);

    List<DocumentVerification> findByDocumentDocumentId(Long documentId);
}
