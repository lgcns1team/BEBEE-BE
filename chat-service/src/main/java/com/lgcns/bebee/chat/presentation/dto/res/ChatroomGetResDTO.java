package com.lgcns.bebee.chat.presentation.dto.res;

import com.lgcns.bebee.chat.application.GetChatroomUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "채팅방 조회 응답")
public record ChatroomGetResDTO(
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
        BigDecimal otherSweetness
) {
    public static ChatroomGetResDTO from(GetChatroomUseCase.Result result) {
        return ChatroomGetResDTO.builder()
                .chatroomId(result.getChatroomId())
                .myId(result.getMyId())
                .otherId(result.getOtherId())
                .otherNickname(result.getOtherNickname())
                .otherProfileImageUrl(result.getOtherProfileImageUrl())
                .otherSweetness(result.getOtherSweetness())
                .build();
    }
}
