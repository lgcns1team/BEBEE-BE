package com.lgcns.bebee.file.application;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.file.application.client.PreSignedUrlClient;
import com.lgcns.bebee.file.domain.PresignedUrl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneratePresignedUrlUseCase implements UseCase<GeneratePresignedUrlUseCase.Param, GeneratePresignedUrlUseCase.Result> {
    private final PreSignedUrlClient preSignedUrlClient;

    @Override
    public Result execute(Param param) {
        PresignedUrl url = preSignedUrlClient.generatePresignedUrl(
                param.getDirectory(),
                param.entityId,
                param.originFileName,
                param.getContentType());

        return new Result(
                url.uploadUrl(),
                url.fileUrl(),
                url.expiresIn()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String directory;
        private final String entityId;
        private final String originFileName;
        private final String contentType;
    }

    @Getter
    @AllArgsConstructor
    public static class Result {
        private final String uploadUrl;
        private final String fileUrl;
        private final long expiresIn;
    }
}