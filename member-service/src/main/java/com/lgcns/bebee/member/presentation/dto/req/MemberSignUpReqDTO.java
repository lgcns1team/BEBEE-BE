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
}

