package com.lgcns.bebee.file.presentation.swagger;

import com.lgcns.bebee.file.presentation.dto.req.PresignedUrlReqDTO;
import com.lgcns.bebee.file.presentation.dto.res.PresignedUrlResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "File", description = "파일 업로드 관련 API")
public interface FileSwagger {

    @Operation(
            summary = "Presigned URL 생성",
            description = """
                    S3에 파일을 직접 업로드하기 위한 Presigned URL을 생성합니다.

                    **사용 방법:**
                    1. 이 API를 호출하여 uploadUrl과 fileUrl을 받습니다.
                       - directory: 파일 카테고리 (posts, profiles, chats 등)
                       - entityId: 파일이 속한 엔티티의 ID (게시글 ID, 사용자 ID 등)
                       - originFileName: 파일 원본 이름
                       - contentType: 파일의 MIME 타입
                    2. 클라이언트에서 uploadUrl로 PUT 요청을 보내 파일을 S3에 직접 업로드합니다.
                    3. 업로드 완료 후 fileUrl을 사용하여 파일에 접근하거나 다른 API(게시글 작성 등)에 전달합니다.

                    **장점:**
                    - 백엔드 서버를 거치지 않고 S3로 직접 업로드 (빠른 속도)
                    - 서버 부하 감소
                    - 대용량 파일 업로드에 유리

                    **주의사항:**
                    - Presigned URL은 서버 설정 시간(기본 10분) 후 만료됩니다.
                    - 만료 전에 파일 업로드를 완료해야 합니다.
                    - Content-Type을 요청 시 지정한 값과 동일하게 설정해야 합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Presigned URL 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PresignedUrlResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 필드 누락, 유효하지 않은 파일 타입 등)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<PresignedUrlResDTO> generatePresignedUrl(
            @RequestBody(
                    description = "Presigned URL 생성 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PresignedUrlReqDTO.class)
                    )
            )
            PresignedUrlReqDTO reqDTO
    );
}