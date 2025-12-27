package com.lgcns.bebee.file.presentation.dto.res;

import com.lgcns.bebee.file.application.GeneratePresignedUrlUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Presigned URL 생성 응답 DTO")
public record PresignedUrlResDTO(
        @Schema(
                description = "파일 업로드용 Presigned URL (PUT 요청 사용)",
                example = "https://bebee-storage.s3.ap-northeast-2.amazonaws.com/posts/image.jpg?X-Amz-Algorithm=..."
        )
        String uploadUrl,

        @Schema(
                description = "업로드 완료 후 파일 접근 URL",
                example = "https://bebee-storage.s3.ap-northeast-2.amazonaws.com/posts/image.jpg"
        )
        String fileUrl,

        @Schema(
                description = "URL 만료 시간 (초 단위)",
                example = "600"
        )
        long expiresIn
) {
    public static PresignedUrlResDTO from(GeneratePresignedUrlUseCase.Result result) {
        return new PresignedUrlResDTO(
                result.getUploadUrl(),
                result.getFileUrl(),
                result.getExpiresIn()
        );
    }
}