package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.GetChatMessagesUseCase;
import com.lgcns.bebee.chat.application.GetChatroomsUseCase;
import com.lgcns.bebee.chat.application.OpenChatroomUseCase;
import com.lgcns.bebee.chat.presentation.dto.req.ChatroomOpenReqDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomOpenResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomsGetResDTO;
import com.lgcns.bebee.chat.presentation.swagger.ChatroomSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatroomController implements ChatroomSwagger {
    private final GetChatMessagesUseCase getChatMessagesUseCase;
    private final GetChatroomsUseCase getChatroomsUseCase;
    private final OpenChatroomUseCase openChatroomUseCase;

    @GetMapping("/chats")
    public ResponseEntity<ChatMessagesGetResDTO> getChatMessages(
            @RequestParam String chatroomId,
            @RequestParam(required = false) String lastChatId,
            @RequestParam(defaultValue = "20") Integer count
    ) {
        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(Long.parseLong(chatroomId), Long.parseLong(lastChatId), count);
        GetChatMessagesUseCase.Result result = getChatMessagesUseCase.execute(param);

        ChatMessagesGetResDTO response = ChatMessagesGetResDTO.from(
                result.getMessages(),
                result.isHasNext(),
                result.getNextChatId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ChatroomsGetResDTO> getChatrooms(
            @RequestParam Long currentMemberId,
            @RequestParam(required = false) Long lastChatroomId,
            @RequestParam(defaultValue = "20") Integer count
    ) {
        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(currentMemberId, lastChatroomId, count);
        GetChatroomsUseCase.Result result = getChatroomsUseCase.execute(param);

        ChatroomsGetResDTO response = ChatroomsGetResDTO.from(result, currentMemberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ChatroomOpenResDTO> openChatroom(
            @RequestParam(required = false) String chatroomId,
            @RequestParam String currentMemberId,
            @RequestParam(required = false) String otherMemberId,
            @RequestBody ChatroomOpenReqDTO reqDTO
    ){
        OpenChatroomUseCase.Param param = new OpenChatroomUseCase.Param(
                Long.parseLong(chatroomId),
                Long.parseLong(currentMemberId), Long.parseLong(otherMemberId),
                reqDTO.postId(), reqDTO.postTitle(), reqDTO.helpCategoryIds()
        );

        OpenChatroomUseCase.Result result = openChatroomUseCase.execute(param);

        ChatroomOpenResDTO resDTO = ChatroomOpenResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}

