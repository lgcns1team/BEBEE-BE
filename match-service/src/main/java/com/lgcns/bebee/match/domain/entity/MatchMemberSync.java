package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.AccountStatus;
import com.lgcns.bebee.match.domain.entity.vo.Gender;
import com.lgcns.bebee.match.domain.entity.vo.MemberRole;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 회원 Sync 테이블
 */
@Entity
@Table(name = "match_member_sync")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchMemberSync extends BaseTimeEntity {
    @Id
    @Tsid
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 255)
    private String addressRoad;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false, length = 10)
    private String districtCode;
}
