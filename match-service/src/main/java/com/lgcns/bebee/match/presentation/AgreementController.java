package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.application.usecase.GetTotalHoneyUseCase;
import com.lgcns.bebee.match.presentation.dto.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.AgreementCreateResDTO;
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
@RequestMapping("/api/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final CreateAgreementUseCase createAgreementUseCase;
    private final GetTotalHoneyUseCase getTotalHoneyUseCase;

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

    @Operation(summary = "전체 꿀 개수 계산", description = "단위 꿀 개수에 대해 총 지급 꿀 개수를 계산합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 꿀 개수 계산 성공"),
            @ApiResponse(responseCode = "404", description = "매칭 확인서를 찾을 수 없음")
    })
    @PostMapping("/{agreementId}/totalhoney")
    public ResponseEntity<GetTotalHoneyUseCase.Result> calculateTotalHoney(@PathVariable Long agreementId) {

        GetTotalHoneyUseCase.Param param = new GetTotalHoneyUseCase.Param(agreementId);

        GetTotalHoneyUseCase.Result result = getTotalHoneyUseCase.execute(param);

        return ResponseEntity.ok(result);
    }
}
