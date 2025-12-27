package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberSync, Long> {
}
