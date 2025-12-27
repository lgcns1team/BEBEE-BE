package com.lgcns.bebee.file.presentation;

import com.lgcns.bebee.file.application.GeneratePresignedUrlUseCase;
import com.lgcns.bebee.file.presentation.dto.req.PresignedUrlReqDTO;
import com.lgcns.bebee.file.presentation.dto.res.PresignedUrlResDTO;
import com.lgcns.bebee.file.presentation.swagger.FileSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController implements FileSwagger {

    private final GeneratePresignedUrlUseCase generatePresignedUrlUseCase;

    @Override
    @PostMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResDTO> generatePresignedUrl(
            @RequestBody PresignedUrlReqDTO reqDTO
    ) {
        GeneratePresignedUrlUseCase.Param param = reqDTO.toParam();
        GeneratePresignedUrlUseCase.Result result = generatePresignedUrlUseCase.execute(param);
        PresignedUrlResDTO resDTO = PresignedUrlResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}