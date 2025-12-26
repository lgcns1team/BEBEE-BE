package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseTimeEntity {
    @Id @Tsid
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private int sequence;

    public static PostImage create(String imageUrl, int sequence){
        PostImage image = new PostImage();
        image.imageUrl = imageUrl;
        image.sequence = sequence;

        return image;
    }

    protected void assignToPost(Post post){
        this.post = post;
    }
}
