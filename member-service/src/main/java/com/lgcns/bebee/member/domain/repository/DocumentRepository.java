package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 문서 Repository
 */
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByDocCode(String docCode);

    List<Document> findByMemberId(Long memberId);

    List<Document> findByTargetRole(String targetRole);
}

