package com.lgcns.bebee.match.domain.repository.dto;

import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSyncRepository extends JpaRepository<MemberSync, Long> {
}
