package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.ConfirmAgreementUseCase;
import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.application.usecase.RefuseAgreementUseCase;
import com.lgcns.bebee.match.presentation.dto.req.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.AgreementRefuseReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementConfirmReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementConfirmResDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementCreateResDTO;
import com.lgcns.bebee.match.presentation.swagger.AgreementSwagger;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class AgreementController implements AgreementSwagger {

    private final CreateAgreementUseCase createAgreementUseCase;
    private final RefuseAgreementUseCase refuseAgreementUseCase;
    private final ConfirmAgreementUseCase confirmAgreementUseCase;

    @PostMapping
    public ResponseEntity<AgreementCreateResDTO> createAgreement(@RequestBody AgreementCreateReqDTO request) {

        CreateAgreementUseCase.Param param = request.toParam();

        CreateAgreementUseCase.Result result = createAgreementUseCase.execute(param);

        AgreementCreateResDTO response = AgreementCreateResDTO.from(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{agreementId}/refuse")
    public ResponseEntity<Void> refuseAgreement(
            @PathVariable Long agreementId,
            @RequestBody AgreementRefuseReqDTO request
            ) {

        RefuseAgreementUseCase.Param param = request.toParam(agreementId);
        refuseAgreementUseCase.execute(param);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{agreementId}/confirm")
    public ResponseEntity<AgreementConfirmResDTO> confirmAgreement(
            @PathVariable Long agreementId,
            @RequestBody AgreementConfirmReqDTO request
    ) {
        ConfirmAgreementUseCase.Param param = request.toParam(agreementId);

        ConfirmAgreementUseCase.Result result = confirmAgreementUseCase.execute(param);

        AgreementConfirmResDTO response = AgreementConfirmResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}
