package com.lgcns.bebee.chat.presentation.swagger;

import com.lgcns.bebee.chat.presentation.dto.req.ChatroomOpenReqDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomOpenResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomsGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
                    example = "1"
            )
            @RequestParam Long chatroomId,

            @Parameter(
                    description = """
                            마지막으로 조회한 메시지 ID (커서 페이징용)
                            - null: 최신 메시지부터 조회
                            - 값 있음: 해당 ID 이전의 메시지 조회
                            """,
                    example = "1234567890"
            )
            @RequestParam(required = false) Long lastChatId,

            @Parameter(
                    description = "한 번에 조회할 메시지 개수",
                    example = "20"
            )
            @RequestParam(defaultValue = "20") Integer count
    );

    @Operation(
            summary = "채팅방 목록 조회",
            description = """
                    현재 사용자의 채팅방 목록을 커서 기반 페이징으로 조회합니다.

                    - 최초 조회 시 lastChatroomId를 전달하지 않으면 최신 채팅방부터 조회됩니다.
                    - 다음 페이지를 불러오려면 응답의 nextChatroomId를 다음 요청의 lastChatroomId로 전달하세요.
                    - hasNext가 true이면 더 불러올 채팅방이 있다는 의미입니다.
                    - 각 채팅방에는 상대방 정보와 마지막 메시지, 최근 업데이트 시간이 포함됩니다.
                    - 아직 읽지 않은 채팅 수는 포함되지 않습니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatroomsGetResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<ChatroomsGetResDTO> getChatrooms(
            @Parameter(
                    description = "현재 사용자(본인) ID (임시 용도, 나중에 토큰에서 처리)",
                    required = true,
                    example = "100"
            )
            @RequestParam Long currentMemberId,

            @Parameter(
                    description = """
                            마지막으로 조회한 채팅방 ID (커서 페이징용)
                            - null: 최신 채팅방부터 조회
                            - 값 있음: 해당 ID 이전의 채팅방 조회
                            """,
                    example = "1234567890"
            )
            @RequestParam(required = false) Long lastChatroomId,

            @Parameter(
                    description = "한 번에 조회할 채팅방 개수",
                    example = "20"
            )
            @RequestParam(defaultValue = "20") Integer count
    );

    @Operation(
            summary = "채팅방 열기 (조회/생성)",
            description = """
                    기존 채팅방이 있으면 조회, 없으면 채팅방 생성
                    
                    지도 페이지에서 채팅방 버튼 클릭: chatroomId = null, 게시글 정보 X
                    매칭 현황 페이지에서 채팅방 버튼 클릭: chatroomId 존재
                    도우미 지원 현황 페이지에서 채팅방 버튼 클릭: chatroomId = null, 게시글 정보 O(** 중요 ** 이때, 게시글 정보를 줘야 합니다.)
                    채팅방 리스트에서 채팅방 진입: chatroomId 존재

                    **동작 방식:**
                    1. chatroomId가 있는 경우: 기존 채팅방 조회
                    2. chatroomId가 없는 경우: memberId 로 채팅방을 찾거나 새로 생성
                    3. 게시글 정보(postId, postTitle)와 도움 카테고리를 채팅방에 연결

                    **요청 파라미터:**
                    - chatroomId: 기존 채팅방 ID (선택)
                    - currentMemberId: 현재 사용자 ID (필수)
                    - otherMemberId: 상대방 ID (chatroomId가 없을 때 필수)

                    **요청 본문 (ChatroomOpenReqDTO):**
                    - postId: 게시글 ID
                    - postTitle: 게시글 제목
                    - helpCategoryIds: 도움 카테고리 ID 목록

                    **응답 정보:**
                    - 채팅방 ID, 본인 ID, 상대방 정보 (ID, 닉네임, 프로필 이미지, 당도)
                    - 연결된 도움 카테고리 목록
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 열기 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatroomOpenResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원 또는 채팅방을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<ChatroomOpenResDTO> openChatroom(
            @Parameter(
                    description = "기존 채팅방 ID (선택)",
                    example = "1"
            )
            @RequestParam(required = false) Long chatroomId,

            @Parameter(
                    description = "현재 사용자(본인) ID (필수, 임시 용도, 나중에 토큰에서 처리)",
                    required = true,
                    example = "100"
            )
            @RequestParam Long currentMemberId,

            @Parameter(
                    description = "상대방 ID (chatroomId가 없을 때 필수)",
                    example = "200"
            )
            @RequestParam(required = false) Long otherMemberId,

            @Parameter(
                    description = """
                            채팅방 열기 요청 정보
                            - postId: 게시글 ID
                            - postTitle: 게시글 제목
                            - helpCategoryIds: 도움 카테고리 ID 목록
                            """,
                    required = true
            )
            @RequestBody ChatroomOpenReqDTO reqDTO
    );
}
