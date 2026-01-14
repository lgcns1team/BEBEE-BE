package com.lgcns.bebee.member.presentation.dto.req;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignUpReqDTO {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private String gender;
    private String phoneNumber;
    private String role;
    private String addressRoad;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String districtCode;

    // HELPER/DISABLED 공통: 도움 유형 목록, 자기소개
    private java.util.List<String> helpTypes;
    private String introduction;

    // DISABLED용: 장애 유형, 등급 및 설명
    private String disabilityType;
    private String disabilityGrade;         // "1" = 중증, "2" = 경증
    private String disabilityDescription;

    // 문서 관련 (Step 5에서 업로드 및 분석 완료)
    private String fileUrl;
    private String systemFlag;
}
