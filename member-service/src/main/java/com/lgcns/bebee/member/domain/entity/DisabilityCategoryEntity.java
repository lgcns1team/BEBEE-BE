package com.lgcns.bebee.member.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "disability_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisabilityCategoryEntity {

    @Id
    private Long disabilityCategoryId;

    @Column(nullable = false, length = 10, unique = true)
    private String type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

