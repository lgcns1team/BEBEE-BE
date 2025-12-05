package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Id
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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
