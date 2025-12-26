package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.CreatePostUseCase;

public record PostCreateResDTO(
        String postId
) {
    public static PostCreateResDTO from(CreatePostUseCase.Result result) {
        return new PostCreateResDTO(String.valueOf(result.getPostId()));
    }
}
