package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import lombok.extern.slf4j.Slf4j;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import com.lgcns.bebee.member.domain.entity.DisabilityCategory;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.HelpCategory;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.MemberHelpCategory;
import com.lgcns.bebee.member.domain.repository.DisabilityCategoryRepository;
import com.lgcns.bebee.member.domain.repository.DocumentRepository;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.repository.HelpCategoryRepository;
import com.lgcns.bebee.member.domain.repository.MemberDisabilityCategoryRepository;
import com.lgcns.bebee.member.domain.repository.MemberHelpCategoryRepository;
import com.lgcns.bebee.member.domain.service.MemberManagement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpUseCase implements UseCase<SignUpUseCase.Param, SignUpUseCase.Result> {
    private final MemberRepository memberRepository;
    private final MemberManagement memberManagement;
    private final HelpCategoryRepository helpCategoryRepository;
    private final DisabilityCategoryRepository disabilityCategoryRepository;
    private final MemberHelpCategoryRepository memberHelpCategoryRepository;
    private final MemberDisabilityCategoryRepository memberDisabilityCategoryRepository;
    private final DocumentRepository documentRepository;
    private final DocumentVerificationRepository documentVerificationRepository;

    @Override
    @Transactional
    public SignUpUseCase.Result execute(Param params) {
        params.validate();

        memberManagement.checkEmailDuplicated(params.getEmail());
        memberManagement.checkNicknameDuplicated(params.getNickname());

        Member newMember = memberManagement.createMember(
                params.getEmail(),
                params.getPassword(),
                params.getName(),
                params.getNickname(),
                params.getBirthDate(),
                params.getGender(),
                params.getPhoneNumber(),
                params.getRole(),
                params.getAddressRoad(),
                params.getLatitude(),
                params.getLongitude(),
                params.getDistrictCode(),
                params.getIntroduction());

        Member savedMember = memberRepository.save(newMember);

        // HELPER/DISABLED: 도움 유형 저장 (두 역할 모두 필요)
        if ((params.getRole().equals("HELPER") || params.getRole().equals("DISABLED")) 
                && params.getHelpTypes() != null && !params.getHelpTypes().isEmpty()) {
            for (String helpTypeName : params.getHelpTypes()) {
                StringBuilder hexBuilder = new StringBuilder();
                for (byte b : helpTypeName.getBytes()) {
                    hexBuilder.append(String.format("%02X", b));
                }
                log.info("도움 유형 조회 요청: [{}], Hex: {}", helpTypeName, hexBuilder.toString());

                HelpCategory helpCategory = helpCategoryRepository
                        .findByHelpType(helpTypeName)
                        .orElseThrow(() -> new IllegalArgumentException("도움 유형을 찾을 수 없습니다: " + helpTypeName));
                MemberHelpCategory memberHelpCategory = MemberHelpCategory.create(savedMember, helpCategory);
                memberHelpCategoryRepository.save(memberHelpCategory);
            }
        }

        // DISABLED: 장애 유형 저장
        if ("DISABLED".equals(params.getRole()) && params.getDisabilityType() != null
                && !params.getDisabilityType().isBlank()) {
            DisabilityCategory disabilityCategory = disabilityCategoryRepository
                    .findByType(params.getDisabilityType())
                    .orElseThrow(() -> new IllegalArgumentException("장애 유형을 찾을 수 없습니다: " + params.getDisabilityType()));
            MemberDisabilityCategory memberDisabilityCategory = MemberDisabilityCategory.create(
                    savedMember,
                    disabilityCategory,
                    params.getDisabilityGrade() != null ? params.getDisabilityGrade() : "1",
                    params.getDisabilityDescription() != null ? params.getDisabilityDescription() : "");
            memberDisabilityCategoryRepository.save(memberDisabilityCategory);
        }

        // 문서 검증 정보 저장 (Step 5에서 이미 분석 완료됨)
        if (params.getFileUrl() != null && !params.getFileUrl().isBlank()) {
            // role 검증
            if (!java.util.Set.of("HELPER", "DISABLED").contains(params.getRole())) {
                throw new IllegalArgumentException("문서 저장 시 유효한 role이 필요합니다: " + params.getRole());
            }
            
            log.info("문서 검증 정보 저장 시작: fileUrl={}, systemFlag={}", params.getFileUrl(), params.getSystemFlag());
            
            // Document 생성
            String docCode = "DOC_" + java.util.UUID.randomUUID().toString().substring(0, 8);
            String docNameKo = "HELPER".equals(params.getRole()) ? "활동지원사 교육 이수증" : "장애인 복지카드";
            Document document = Document.create(
                    params.getRole(),
                    docCode,
                    docNameKo,
                    docNameKo,
                    savedMember);
            documentRepository.save(document);

            // DocumentVerification 생성 (분석은 이미 완료됨, systemFlag만 저장)
            DocumentVerification verification = DocumentVerification.of(params.getFileUrl(), document);
            verification.applyAnalysisResult(0, 0, 0, params.getSystemFlag() != null ? params.getSystemFlag() : "MID");
            documentVerificationRepository.save(verification);
            
            log.info("문서 검증 정보 저장 완료: verificationId={}", verification.getId());
        }

        return new Result(savedMember.getId());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String email;
        private final String password;
        private final String name;
        private final String nickname;
        private final LocalDate birthDate;
        private final String gender;
        private final String phoneNumber;
        private final String role;
        private final String addressRoad;
        private final BigDecimal latitude;
        private final BigDecimal longitude;
        private final String districtCode;

        // HELPER/DISABLED 공통: 도움 유형 목록, 자기소개
        private final java.util.List<String> helpTypes;
        private final String introduction;

        // DISABLED용: 장애 유형, 등급 및 설명
        private final String disabilityType;
        private final String disabilityGrade;       // "1" = 중증, "2" = 경증
        private final String disabilityDescription;

        // 문서 관련 (Step 5에서 업로드 및 분석 완료)
        private final String fileUrl;
        private final String systemFlag;

        @Override
        public boolean validate() {
            validateEmail();
            validateNickname();
            validatePassword();
            return true;
        }

        private void validateEmail() {
            if (email == null || email.isBlank()) {
                throw MemberInvalidParamErrors.EMAIL_NOT_NULL.toException();
            }
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            if (!emailPattern.matcher(email).matches()) {
                throw MemberInvalidParamErrors.INVALID_EMAIL_FORMAT.toException();
            }
        }

        private void validateNickname() {
            if (nickname == null || nickname.isBlank()) {
                throw MemberInvalidParamErrors.NICKNAME_NOT_NULL.toException();
            }

        }

        private void validatePassword() {
            if (password == null || password.isBlank()) {
                throw MemberInvalidParamErrors.PASSWORD_NOT_NULL.toException();
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final Long memberId;
    }

    public boolean checkEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
// Force git tracking for conflict resolution
