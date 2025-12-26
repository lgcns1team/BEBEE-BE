package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.repository.dto.MemberSyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberManager {
    private final MemberSyncRepository memberSyncRepository;

    @Transactional(readOnly = true)
    public MemberSync findExistingMember(Long memberId) {
        return memberSyncRepository.findById(memberId).orElseThrow(MatchErrors.MEMBER_NOT_FOUND::toException);
    }
}
