package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatErrors;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.MemberSyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberManagement {
    private final MemberSyncRepository memberSyncRepository;;

    @Transactional(readOnly = true)
    public MemberSync getExistingMember(Long memberId){
        return memberSyncRepository.findById(memberId).orElseThrow(ChatErrors.MEMBER_NOT_FOUND::toException);
    }
}
