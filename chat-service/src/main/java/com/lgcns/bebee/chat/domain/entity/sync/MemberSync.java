package com.lgcns.bebee.chat.domain.entity.sync;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSync implements Persistable<Long> {
    @Id
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String profileImageUrl;

    private BigDecimal sweetness;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public static MemberSync create(
            Long memberId,
            String nickname,
            String profileImageUrl
    ){
        MemberSync member = new MemberSync();
        member.id = memberId;
        member.nickname = nickname;
        member.profileImageUrl = profileImageUrl;
        member.createdAt = LocalDateTime.now();
        member.updatedAt = LocalDateTime.now();
        member.sweetness = BigDecimal.valueOf(0.0);
        return member;
    }

    @Transient
    private Boolean isNew = true;

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PostPersist
    @PostLoad
    protected void markNotNew() {
        this.isNew = false;
    }
}
