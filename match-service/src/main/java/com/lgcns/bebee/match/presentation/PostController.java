package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CreatePostUseCase;
import com.lgcns.bebee.match.application.usecase.GetPostsUseCase;
import com.lgcns.bebee.match.application.usecase.GetSinglePostUseCase;
import com.lgcns.bebee.match.presentation.dto.req.PostCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.PostsGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostsGetResDTO;
import com.lgcns.bebee.match.presentation.swagger.PostSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostSwagger {
    private final CreatePostUseCase createPostUseCase;
    private final GetPostsUseCase getPostsUseCase;
    private final GetSinglePostUseCase getSinglePostUseCase;

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

    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResDTO> getSinglePost(
            @RequestParam String currentMemberId,
            @PathVariable String postId
    ){
        GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(
                Long.parseLong(currentMemberId),
                Long.parseLong(postId)
        );
        GetSinglePostUseCase.Result result = getSinglePostUseCase.execute(param);
        PostGetResDTO resDTO = PostGetResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }

    @Override
    @PostMapping
    public ResponseEntity<PostCreateResDTO> createPost(
            @RequestParam String currentMemberId,
            @RequestBody PostCreateReqDTO reqDTO
    ){
        CreatePostUseCase.Param param = reqDTO.toParam(currentMemberId);
        CreatePostUseCase.Result result = createPostUseCase.execute(param);
        PostCreateResDTO resDTO = PostCreateResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }
}
