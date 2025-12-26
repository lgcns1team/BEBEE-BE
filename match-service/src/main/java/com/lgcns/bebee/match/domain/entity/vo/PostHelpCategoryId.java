package com.lgcns.bebee.match.domain.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class PostHelpCategoryId implements Serializable {
    private Long postId;
    private Long helpCategoryId;

    public PostHelpCategoryId(Long postId, Long helpCategoryId) {
        this.postId = postId;
        this.helpCategoryId = helpCategoryId;
    }
}
