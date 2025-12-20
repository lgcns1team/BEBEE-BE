package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
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
    @Tsid
    private Long memberId;

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

    /**
     * 회원 생성 팩토리 메서드
     * 도메인 서비스 및 테스트 코드에서 일관된 방식으로 Member 인스턴스를 생성하기 위해 사용합니다.
     */
    public static Member createNewMember(String email,
                                         String encodedPassword,
                                         String name,
                                         String nickname,
                                         LocalDate birthDate,
                                         Gender gender,
                                         String phoneNumber,
                                         Role role,
                                         MemberStatus status,
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
        member.gender = gender;
        member.phoneNumber = phoneNumber;
        member.role = role;
        member.status = status;
        member.addressRoad = addressRoad;
        member.latitude = latitude;
        member.longitude = longitude;
        member.districtCode = districtCode;
        // profileImageUrl, introduction, sweetness 는 기본값 사용
        return member;
    }
}

