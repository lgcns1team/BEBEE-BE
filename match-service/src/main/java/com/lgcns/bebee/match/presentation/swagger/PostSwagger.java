package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.req.PostCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.PostsGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostsGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Post", description = "게시글 관련 API")
public interface PostSwagger {

    @Operation(
            summary = "게시글 목록 조회",
            description = """
                    필터 조건을 기반으로 게시글 목록을 조회합니다.

                    **주요 필터 조건:**
                    - 도움 타입 (DAY: 일회성, TERM: 정기적)
                    - 매칭 완료 여부 (isMatched: false=매칭 전, null=전체)
                    - 지역 코드 (법정동 코드 기반)
                    - 도움 카테고리 (외출동행, 방문목욕, 방문간호 등)
                    - 성별 선호도
                    - 꿀 범위 (보상 금액)
                    - 활동 가능 요일

                    **무한 스크롤:**
                    - 커서 기반 페이지네이션 (postId 기반)
                    - 최초 요청 시 lastPostId=null, count=20
                    - 다음 페이지 요청 시 이전 응답의 nextPostId를 lastPostId로 전달
                    - hasNext=false면 마지막 페이지

                    **응답:**
                    - 각 게시글의 기본 정보 (제목, 꿀, 지역, 카테고리, 이미지 등) 포함
                    - nextPostId: 다음 페이지 커서
                    - hasNext: 다음 페이지 존재 여부
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostsGetResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<PostsGetResDTO> getPosts(
            @Parameter(
                    description = "현재 로그인한 회원 ID(임시, 나중에 토큰으로 처리)",
                    required = true,
                    example = "100"
            )
            @RequestParam String currentMemberId,

            @Parameter(
                    description = """
                            도움 요청 타입
                            - DAY: 일회성 도움 (하루 단위)
                            - TERM: 정기적 도움 (기간 단위)
                            - 전체면 보내지 마세요!!
                            """,
                    required = false,
                    example = "DAY"
            )
            @RequestParam String type,

            @Parameter(
                    description = """
                            매칭 완료 여부 (선택)
                            - false: 매칭 완료되지 않은 게시글만 조회 (NON_MATCHED 상태)
                            - null: 전체 조회 (상태 필터 없음)
                            """,
                    required = false,
                    example = "false"
            )
            @RequestParam(required = false) Boolean isMatched,

            @Parameter(
                    description = """
                            커서 (무한 스크롤용) - 이전 응답의 nextPostId 값 사용
                            - 최초 요청 시에는 null 또는 생략
                            - 다음 페이지 요청 시 이전 응답의 nextPostId 값을 문자열로 전달
                            - postId 기반 커서 페이지네이션
                            """,
                    required = false,
                    example = "1050"
            )
            @RequestParam(required = false) String lastPostId,

            @Parameter(
                    description = """
                            페이지당 게시글 수
                            - 기본값: 20
                            - 최대값: 100 (초과 시 자동으로 100으로 제한)
                            """,
                    required = false,
                    example = "20"
            )
            @RequestParam(defaultValue = "20") Integer count,

            @Parameter(
                    description = "게시글 필터 조건 (선택적 파라미터들)",
                    required = false
            )
            @ModelAttribute PostsGetReqDTO reqDTO
    );

    @Operation(
            summary = "게시글 상세 조회",
            description = """
                    특정 게시글의 상세 정보를 조회합니다.

                    **응답 정보:**
                    - 회원 정보: 닉네임, 지역 코드, 프로필 이미지
                    - 게시글 기본 정보: 제목, 도움 타입, 단위 꿀, 총 꿀, 내용
                    - 도움 카테고리 목록
                    - 날짜/시간 정보:
                      - DAY 타입: date 필드에 단일 날짜 포함
                      - TERM 타입: startDate, endDate 필드에 기간 포함
                    - 스케줄 정보: 요일별 시간 (TERM 타입의 경우)
                    - 게시글 이미지 목록 (순서대로 정렬)
                    - 신청자 수
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostGetResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<PostGetResDTO> getSinglePost(
            @Parameter(
                    description = "현재 로그인한 회원 ID (임시, 나중에 토큰으로 처리)",
                    required = true,
                    example = "100"
            )
            @RequestParam String currentMemberId,

            @Parameter(
                    description = "조회할 게시글 ID",
                    required = true,
                    example = "1234567890"
            )
            @PathVariable String postId
    );

    @Operation(
            summary = "게시글 작성",
            description = """
                    새로운 도움 요청 게시글을 작성합니다.

                    **게시글 타입:**
                    - DAY: 일회성 도움 (단일 날짜, date 필드 사용)
                    - TERM: 정기적 도움 (기간, startDate/endDate 필드 사용)

                    **필수 정보:**
                    - 게시글 기본 정보: 제목, 내용, 도움 타입
                    - 보상 정보: 단위 꿀, 총 꿀
                    - 위치 정보: 지역, 법정동 코드, 위도, 경도
                    - 도움 카테고리: 1개 이상 선택 필요
                    - 스케줄: 활동 가능한 요일/시간

                    **선택 정보:**
                    - 게시글 이미지 (최대 3개)
                    - date: DAY 타입의 경우 활동 날짜
                    - startDate: Term 타입의 경우 시작 날짜
                    - endDate: Term 타입의 경우 종료 날짜

                    **응답:**
                    - 생성된 게시글 ID 반환
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 필드 누락, 유효하지 않은 값 등)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<PostCreateResDTO> createPost(
            @Parameter(
                    description = "현재 로그인한 회원 ID (임시, 나중에 토큰으로 처리)",
                    required = true,
                    example = "100"
            )
            @RequestParam String currentMemberId,

            @RequestBody(
                    description = "게시글 작성 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostCreateReqDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody PostCreateReqDTO reqDTO
    );
}
