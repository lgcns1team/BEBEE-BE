package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.MatchMemberSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MatchMemberSync, Long> {
}
