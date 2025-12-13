package com.lgcns.bebee.chat.presentation.swagger;

import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "채팅방", description = "채팅방 관련 API")
public interface ChatroomSwagger {

    @Operation(
            summary = "채팅 메시지 조회",
            description = """
                    특정 채팅방의 메시지 목록을 커서 기반 페이징으로 조회합니다.

                    - 최초 조회 시 lastChatId를 전달하지 않으면 최신 메시지부터 조회됩니다.
                    - 이전 메시지를 더 불러오려면 응답의 nextChatId를 다음 요청의 lastChatId로 전달하세요.
                    - hasNext가 true이면 더 불러올 메시지가 있다는 의미입니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "메시지 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessagesGetResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "채팅방을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<ChatMessagesGetResDTO> getChatMessages(
            @Parameter(
                    description = "채팅방 ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long chatroomId,

            @Parameter(
                    description = """
                            마지막으로 조회한 메시지 ID (커서 페이징용)
                            - null: 최신 메시지부터 조회
                            - 값 있음: 해당 ID 이전의 메시지 조회
                            """,
                    required = false,
                    example = "1234567890"
            )
            @RequestParam(required = false) Long lastChatId,

            @Parameter(
                    description = "한 번에 조회할 메시지 개수",
                    example = "20"
            )
            @RequestParam(defaultValue = "20") Integer count
    );
}
