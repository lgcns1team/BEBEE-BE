package com.lgcns.bebee.chat.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "채팅방 열기 요청")
public record ChatroomOpenReqDTO(
        Long postId,
        String postTitle,
        List<Long> helpCategoryIds
) {
}