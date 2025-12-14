package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.GetChatMessagesUseCase;
import com.lgcns.bebee.chat.application.GetChatroomUseCase;
import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomGetResDTO;
import com.lgcns.bebee.chat.presentation.swagger.ChatroomSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatroomController implements ChatroomSwagger {
    private final GetChatMessagesUseCase getChatMessagesUseCase;
    private final GetChatroomUseCase getChatroomUseCase;

    @GetMapping("/chats")
    public ResponseEntity<ChatMessagesGetResDTO> getChatMessages(
            @RequestParam Long chatroomId,
            @RequestParam(required = false) Long lastChatId,
            @RequestParam(defaultValue = "20") Integer count
    ) {
        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(chatroomId, lastChatId, count);
        GetChatMessagesUseCase.Result result = getChatMessagesUseCase.execute(param);

        ChatMessagesGetResDTO response = ChatMessagesGetResDTO.from(
                result.getMessages(),
                result.isHasNext(),
                result.getNextChatId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ChatroomGetResDTO> getChatroom(
            @RequestParam(required = false) Long chatroomId,
            @RequestParam Long currentMemberId,
            @RequestParam(required = false) Long otherMemberId
    ){
        GetChatroomUseCase.Param param = new GetChatroomUseCase.Param(chatroomId, currentMemberId, otherMemberId);
        GetChatroomUseCase.Result result = getChatroomUseCase.execute(param);

        ChatroomGetResDTO response = ChatroomGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}

