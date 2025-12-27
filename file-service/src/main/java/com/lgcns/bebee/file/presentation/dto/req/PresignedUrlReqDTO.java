package com.lgcns.bebee.file.presentation.dto.req;

import com.lgcns.bebee.file.application.GeneratePresignedUrlUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Presigned URL 생성 요청 DTO")
public record PresignedUrlReqDTO(
        @Schema(
                description = "파일이 저장될 디렉토리 (예: posts, chats, profiles)",
                example = "posts",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String directory,

        @Schema(
                description = "엔티티 ID (게시글 ID, 사용자 ID 등 - 파일이 속한 엔티티 식별자)",
                example = "17123412400123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String entityId,

        @Schema(
                description = "원본 파일명 (확장자 포함, 서버에서 고유한 파일명 생성 시 참조)",
                example = "my-photo.jpg",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String originFileName,

        @Schema(
                description = "파일 MIME 타입",
                example = "image/jpeg",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"image/jpeg", "image/png", "image/gif", "image/webp"}
        )
        String contentType
) {
    public GeneratePresignedUrlUseCase.Param toParam() {
        return new GeneratePresignedUrlUseCase.Param(
                directory,
                entityId,
                originFileName,
                contentType
        );
    }
}
