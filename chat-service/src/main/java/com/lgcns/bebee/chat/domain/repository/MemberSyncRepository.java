package com.lgcns.bebee.chat.domain.repository;

import com.lgcns.bebee.chat.domain.entity.MemberSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSyncRepository extends JpaRepository<MemberSync, Long> {
}
