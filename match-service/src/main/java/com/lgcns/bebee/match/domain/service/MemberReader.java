package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.domain.entity.MatchMemberSync;
import com.lgcns.bebee.match.domain.repository.MemberRepository;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberReader {
    private final MemberRepository matchMemberSyncRepository;

    @Transactional(readOnly = true)
    public MatchMemberSync getById(Long memberId) {
        return matchMemberSyncRepository.findById(memberId)
                .orElseThrow(() -> MatchErrors.MEMBER_NOT_FOUND.toException());
    }
}
