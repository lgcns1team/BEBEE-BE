package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.presentation.dto.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.AgreementCreateResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final CreateAgreementUseCase createAgreementUseCase;

    @PostMapping
    public ResponseEntity<AgreementCreateResDTO> createAgreement(@RequestBody AgreementCreateReqDTO request) {

        CreateAgreementUseCase.Param param = request.toParam();

        CreateAgreementUseCase.Result result = createAgreementUseCase.execute(param);

        AgreementCreateResDTO response = AgreementCreateResDTO.from(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
