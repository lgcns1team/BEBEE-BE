package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberManagement {
    private final MemberRepository memberRepository;

    public Member getExistingMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberErrors.MEMBER_NOT_FOUND::toException);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberErrors.MEMBER_NOT_FOUND_BY_EMAIL::toException);
    }
}
