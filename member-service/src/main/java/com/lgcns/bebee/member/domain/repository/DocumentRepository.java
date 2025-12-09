package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 문서 Repository
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * 문서 코드로 조회
     * @param docCode 문서 코드
     * @return 해당 문서
     */
    Optional<Document> findByDocCode(String docCode);

    /**
     * 특정 회원의 문서 목록 조회
     * @param memberId 회원 ID
     * @return 해당 회원의 문서 목록
     */
    List<Document> findByMemberMemberId(Long memberId);

    /**
     * 대상 역할별 문서 목록 조회
     * @param targetRole 대상 역할 (HELPER, DISABLED 등)
     * @return 해당 역할의 문서 목록
     */
    List<Document> findByTargetRole(String targetRole);
}

