package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @Tsid @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.NONE;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 255)
    private String addressRoad;

    @Column(length = 255)
    private String introduction;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false, length = 10)
    private String districtCode;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal sweetness = BigDecimal.valueOf(40.00);

    public static Member create(String email,
                                String encodedPassword,
                                String name,
                                String nickname,
                                LocalDate birthDate,
                                String gender,
                                String phoneNumber,
                                String role,
                                String addressRoad,
                                BigDecimal latitude,
                                BigDecimal longitude,
                                String districtCode) {
        Member member = new Member();
        member.email = email;
        member.password = encodedPassword;
        member.name = name;
        member.nickname = nickname;
        member.birthDate = birthDate;
        member.gender = Gender.valueOf(gender);
        member.phoneNumber = phoneNumber;
        member.role = Role.from(role);
        member.status = MemberStatus.PENDING_APPROVAL;
        member.addressRoad = addressRoad;
        member.latitude = latitude;
        member.longitude = longitude;
        member.districtCode = districtCode;
        // profileImageUrl, introduction, sweetness 는 기본값 사용
        return member;
    }

    /**
     * 입력된 비밀번호가 회원의 비밀번호와 일치하는지 검증합니다.
     *
     * @param encodedPassword 암호화된 입력 비밀번호
     * @throws com.lgcns.bebee.member.core.exception.MemberException 비밀번호가 일치하지 않는 경우
     */
    public void validatePassword(String encodedPassword) {
        if (!encodedPassword.equals(this.password)) {
            throw MemberErrors.INVALID_PASSWORD.toException();
        }
    }

    /**
     * 회원이 로그인 가능한 상태인지 검증합니다.
     * ACTIVE 상태만 로그인이 가능합니다.
     *
     * @throws com.lgcns.bebee.member.core.exception.MemberException 로그인 불가능한 상태인 경우
     */
    public void validateLoginAvailable() {
        switch (this.status) {
            case REJECTED -> throw MemberErrors.MEMBER_STATUS_REJECTED.toException();
            case WITHDRAWN, WITHDRAW_APPROVAL -> throw MemberErrors.MEMBER_STATUS_WITHDRAWN.toException();
            case ACTIVE, PENDING_APPROVAL -> { /* 정상 */ }
        }
    }
}

