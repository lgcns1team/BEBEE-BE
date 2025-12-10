package com.lgcns.bebee.match.domain.entity.vo;

import java.util.Objects;

public class PostHelpCategoryId {
    private Long postId;
    private Long helpCategoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostHelpCategoryId that = (PostHelpCategoryId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(helpCategoryId, that.helpCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, helpCategoryId);
    }

    // 기본 생성자 (필수)
    public PostHelpCategoryId() {}
}
