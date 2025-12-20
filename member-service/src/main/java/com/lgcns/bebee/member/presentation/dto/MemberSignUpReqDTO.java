package com.lgcns.bebee.member.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor
public class MemberSignUpReqDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 9)
    private String nickname;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotBlank
    private String gender;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String role;

    private String addressRoad;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    @Size(max = 10)
    @Pattern(regexp = "^[0-9A-Za-z]+$")
    private String districtCode;
}

