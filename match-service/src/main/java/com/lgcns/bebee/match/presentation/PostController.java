package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.GetPostsUseCase;
import com.lgcns.bebee.match.presentation.dto.req.PostsGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostsGetResDTO;
import com.lgcns.bebee.match.presentation.swagger.PostSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostSwagger {
    private final GetPostsUseCase getPostsUseCase;

    @Override
    @GetMapping
    public ResponseEntity<PostsGetResDTO> getPosts(
            @RequestParam String currentMemberId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isMatched,
            @RequestParam(required = false) String lastPostId,
            @RequestParam(defaultValue = "20") Integer count,
            @ModelAttribute PostsGetReqDTO reqDTO
            ){
        GetPostsUseCase.Param param = reqDTO.toParam(Long.parseLong(currentMemberId), type, isMatched, lastPostId, count);
        GetPostsUseCase.Result result = getPostsUseCase.execute(param);
        PostsGetResDTO resDTO = PostsGetResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }
}
