package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.common.exception.AuthInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member registerMember(String email,
                                 String encodedPassword,
                                 String name,
                                 String nickname,
                                 LocalDate birthDate,
                                 Gender gender,
                                 Role role,
                                 String phoneNumber,
                                 String addressRoad,
                                 Double latitude,
                                 Double longitude,
                                 String districtCode) {

        validateDuplicate(email, nickname);

        Member member = Member.of(
                email,
                encodedPassword,
                name,
                nickname,
                birthDate,
                gender,
                phoneNumber,
                role,
                MemberStatus.PENDING_APPROVAL,
                addressRoad,
                BigDecimal.valueOf(latitude),
                BigDecimal.valueOf(longitude),
                districtCode
        );

        return memberRepository.save(member);
    }

    private void validateDuplicate(String email, String nickname) {
        if (memberRepository.existsByEmail(email)) {
            throw new InvalidParamException(AuthInvalidParamErrors.EMAIL_ALREADY_EXISTS);
        }
        if (memberRepository.existsByNickname(nickname)) {
            throw new InvalidParamException(AuthInvalidParamErrors.NICKNAME_ALREADY_EXISTS);
        }
    }
}

