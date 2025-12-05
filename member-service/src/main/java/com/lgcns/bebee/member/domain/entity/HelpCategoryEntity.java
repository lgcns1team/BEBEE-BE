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
@Table(name = "help_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpCategoryEntity {

    @Id
    private Long helpCategoryId;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

