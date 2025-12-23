package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.application.usecase.RefuseAgreementUseCase;
import com.lgcns.bebee.match.presentation.dto.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.AgreementCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.AgreementRefuseReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agreement", description = "매칭 확인서 API")
@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final CreateAgreementUseCase createAgreementUseCase;
    private final RefuseAgreementUseCase refuseAgreementUseCase;

    @Operation(summary = "매칭 확인서 생성", description = "새로운 매칭 확인서를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "매칭 확인서 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<AgreementCreateResDTO> createAgreement(@RequestBody AgreementCreateReqDTO request) {

        CreateAgreementUseCase.Param param = request.toParam();

        CreateAgreementUseCase.Result result = createAgreementUseCase.execute(param);

        AgreementCreateResDTO response = AgreementCreateResDTO.from(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "매칭 확인서 거절", description = "도우미가 매칭 확인서를 거절합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매칭 확인서 거절 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @PatchMapping("/{agreementId}/refuse")
    public ResponseEntity<Void> refuseAgreement(
            @PathVariable Long agreementId,
            @RequestBody AgreementRefuseReqDTO request
            ) {

        RefuseAgreementUseCase.Param param = request.toParam(agreementId);
        refuseAgreementUseCase.execute(param);

        return ResponseEntity.ok().build();
    }
}
