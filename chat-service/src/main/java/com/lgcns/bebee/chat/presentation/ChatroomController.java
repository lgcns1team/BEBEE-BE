package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.GetChatMessagesUseCase;
import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import com.lgcns.bebee.chat.presentation.swagger.ChatroomSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatroomController implements ChatroomSwagger {
    private final GetChatMessagesUseCase getChatMessagesUseCase;

    @GetMapping("/{chatroomId}")
    public ResponseEntity<ChatMessagesGetResDTO> getChatMessages(
            @PathVariable Long chatroomId,
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
}
