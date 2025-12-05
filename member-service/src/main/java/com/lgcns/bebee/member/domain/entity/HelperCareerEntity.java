package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.member.domain.entity.vo.CareerType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "helper_career")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelperCareerEntity {

    @Id
    private Long helpCareerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareerType type;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 30)
    private String organization;

    private Integer period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

