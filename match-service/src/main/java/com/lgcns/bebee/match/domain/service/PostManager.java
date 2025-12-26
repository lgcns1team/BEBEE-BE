package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostManager {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Post findSinglePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(MatchErrors.POST_NOT_FOUND::toException);
    }
}
