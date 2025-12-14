package com.lgcns.bebee.chat.presentation.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.chat.application.GetChatroomsUseCase;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "채팅방 목록 조회 응답")
public record ChatroomsGetResDTO(
        @Schema(description = "채팅방 목록", example = "[...]")
        List<ChatroomDTO> chatrooms,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext,

        @Schema(description = "다음 페이지 조회를 위한 커서 ID (hasNext가 true일 때만 제공)", example = "1234567890", nullable = true)
        Long nextChatroomId
) {
    public static ChatroomsGetResDTO from(GetChatroomsUseCase.Result result, Long currentMemberId) {
        List<ChatroomDTO> chatroomDTOs = result.getChatrooms().stream()
                .map(chatroom -> ChatroomDTO.from(chatroom, currentMemberId))
                .toList();

        return ChatroomsGetResDTO.builder()
                .chatrooms(chatroomDTOs)
                .hasNext(result.getHasNext())
                .nextChatroomId(result.getNextChatroomId())
                .build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "채팅방 상세 정보")
    public record ChatroomDTO(
            @Schema(description = "채팅방 ID", example = "1234567890")
            Long chatroomId,

            @Schema(description = "상대방 ID", example = "200")
            Long otherId,

            @Schema(description = "상대방 닉네임", example = "홍길동")
            String otherNickname,

            @Schema(description = "상대방 프로필 이미지 URL", example = "https://example.com/profile.jpg", nullable = true)
            String otherProfileImageUrl,

            @Schema(description = "상대방 당도 점수", example = "40.5")
            BigDecimal otherSweetness,

            @Schema(description = "마지막 메시지", example = "안녕하세요")
            String lastMessage,

            @Schema(description = "최근 업데이트 시간", example = "2024-01-15T10:30:00")
            LocalDateTime updatedAt
    ) {
        public static ChatroomDTO from(Chatroom chatroom, Long currentMemberId) {
            // 현재 사용자와 상대방 구분
            boolean isCurrentMember1 = chatroom.getMember1().getId().equals(currentMemberId);
            MemberSync otherMember = isCurrentMember1
                    ? chatroom.getMember2()
                    : chatroom.getMember1();

            return ChatroomDTO.builder()
                    .chatroomId(chatroom.getId())
                    .otherId(otherMember.getId())
                    .otherNickname(otherMember.getNickname())
                    .otherProfileImageUrl(otherMember.getProfileImageUrl())
                    .otherSweetness(otherMember.getSweetness())
                    .lastMessage(chatroom.getLastMessage())
                    .updatedAt(chatroom.getUpdatedAt())
                    .build();
        }
    }
}
