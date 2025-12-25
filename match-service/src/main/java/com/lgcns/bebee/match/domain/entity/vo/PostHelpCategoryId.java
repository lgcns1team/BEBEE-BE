package com.lgcns.bebee.match.domain.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
public class PostHelpCategoryId implements Serializable {
    private Long postId;
    private Long helpCategoryId;
}
