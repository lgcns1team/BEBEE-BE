package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.repository.dto.PostSearchCond;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findPosts(
            PostSearchCond cond
    );
}