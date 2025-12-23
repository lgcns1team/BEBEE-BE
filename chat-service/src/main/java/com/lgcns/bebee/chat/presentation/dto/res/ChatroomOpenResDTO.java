package com.lgcns.bebee.chat.presentation.dto.res;

import com.lgcns.bebee.chat.application.OpenChatroomUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record ChatroomOpenResDTO(
        @Schema(description = "채팅방 ID", example = "1")
        Long chatroomId,

        @Schema(description = "본인(현재 사용자) ID", example = "100")
        Long myId,

        @Schema(description = "상대방 ID", example = "200")
        Long otherId,

        @Schema(description = "상대방 닉네임", example = "홍길동")
        String otherNickname,

        @Schema(description = "상대방 프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String otherProfileImageUrl,

        @Schema(description = "상대방 당도 점수", example = "85.5")
        BigDecimal otherSweetness,

        @Schema(description = "도움 카테고리 목록")
        List<HelpCategoryRes> helpCategories

) {

    public static ChatroomOpenResDTO from(OpenChatroomUseCase.Result result) {
        List<HelpCategoryRes> helpCategoryInfos = null;
        if (result.getHelpCategories() != null) {
            helpCategoryInfos = result.getHelpCategories().stream()
                    .map(hc -> new HelpCategoryRes(hc.getId(), hc.getName()))
                    .toList();
        }

        return new ChatroomOpenResDTO(
                result.getChatroomId(),
                result.getMyId(),
                result.getOtherId(),
                result.getOtherNickname(),
                result.getOtherProfileImageUrl(),
                result.getOtherSweetness(),
                helpCategoryInfos
        );
    }

    @Schema(description = "도움 카테고리 정보")
    public record HelpCategoryRes(
            @Schema(description = "카테고리 ID", example = "1")
            Long id,

            @Schema(description = "카테고리 이름", example = "이동 지원")
            String name
    ) {
    }
}
