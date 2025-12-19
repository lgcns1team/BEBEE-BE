package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.service.AgreementReader;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import com.lgcns.bebee.match.exception.MatchErrors;
import com.lgcns.bebee.match.exception.MatchInvalidParamErrors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTotalHoneyUseCase implements UseCase<GetTotalHoneyUseCase.Param, GetTotalHoneyUseCase.Result> {
    private final AgreementReader agreementReader;

    @Transactional
    @Override
    public Result execute(Param param) {
        // 1. 파라미터 검증
        param.validate();

        // 2. Agreement 조회
        Agreement agreement = agreementReader.getById(param.getAgreementId());

        // 3. totalHoney 계산
        Integer calculatedTotalHoney = agreement.calculateTotalHoney();

        // 4. Agreement에 업데이트
        agreement.updateTotalHoney(calculatedTotalHoney);

        // 5. 결과 반환 (totalHoney만)
        return Result.from(calculatedTotalHoney);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long agreementId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(agreementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.INVALID_FORMAT, "agreementId");
            }

            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Integer totalHoney;

        public static Result from(Integer totalHoney) {
            return new Result(totalHoney);
        }
    }
}
