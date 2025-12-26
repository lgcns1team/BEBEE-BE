package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.PostHelpCategoryId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHelpCategory extends BaseTimeEntity {
    @EmbeddedId
    private PostHelpCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public static PostHelpCategory create(Long helpCategoryId) {
        PostHelpCategory category = new PostHelpCategory();
        category.id = new PostHelpCategoryId(null, helpCategoryId);
        return category;
    }

    protected void assignToPost(Post post) {
        this.post = post;
    }
}
