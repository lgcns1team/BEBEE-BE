package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefuseAgreementUseCase implements UseCase<RefuseAgreementUseCase.Param, Void> {

    private final AgreementReader agreementReader;

    @Transactional
    @Override
    public Void execute(Param param) {
        param.validate();

        Agreement agreement = agreementReader.getById(param.getAgreementId());

        agreement.refuse();

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long helperId;
        private final Long agreementId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(helperId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "helperId");
            }
            if (!ParamValidator.isValidId(agreementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "agreementId");
            }

            return true;
        }
    }
}
