package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.domain.service.MatchReader;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.exception.MatchErrors;
import com.lgcns.bebee.match.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.presentation.dto.AgreementHelpCategoryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateAgreementUseCase implements UseCase<CreateAgreementUseCase.Param, CreateAgreementUseCase.Result> {
    private final AgreementRepository agreementRepository;
    private final MatchReader matchReader;

    @Transactional
    @Override
    public Result execute(Param param) {
        // 파라미터 검증
        param.validate();

        // 매칭 확인서 생성
        Agreement agreement = Agreement.create(
                param.getType(),
                param.getIsVolunteer(),
                param.getUnitHoney(),
                param.getTotalHoney(),
                param.getRegion(),
                param.getHelpCategoryIds()
        );

        // 매칭 확인서 저장
        Agreement savedAgreement = agreementRepository.save(agreement);

        return Result.from(savedAgreement);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final EngagementType type;
        private final Boolean isVolunteer;
        private final Integer unitHoney;
        private final Integer totalHoney;
        private final String region;
        private final List<Long> helpCategoryIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(memberId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "memberId");
            }
            if (!ParamValidator.isNotNull(type)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "type");
            }
            if (!ParamValidator.isNotNull(isVolunteer)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "isVolunteer");
            }
            if (!ParamValidator.isNonNegativeInteger(unitHoney)) {
                throw new InvalidParamException(MatchInvalidParamErrors.OUT_OF_RANGE, "unitHoney");
            }
            if (!ParamValidator.isNonNegativeInteger(totalHoney)) {
                throw new InvalidParamException(MatchInvalidParamErrors.OUT_OF_RANGE, "totalHoney");
            }
            if (!ParamValidator.isValidString(region)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "region");
            }
            if (!ParamValidator.isValidStringLength(region, 1, 50)) {
                throw new InvalidParamException(MatchInvalidParamErrors.OUT_OF_RANGE, "region");
            }
            if (!ParamValidator.isValidList(helpCategoryIds)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "helpCategoryIds");
            }

            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Long agreementId;
        private AgreementStatus status;
        private LocalDate confirmationDate;
        private EngagementType type;
        private Boolean isVolunteer;
        private List<AgreementHelpCategoryDTO> helpCategories;
        private Integer unitHoney;
        private Integer totalHoney;
        private String region;
        private Boolean isDayComplete;
        private Boolean isTermComplete;

        public static Result from(Agreement agreement) {
            List<AgreementHelpCategoryDTO> categoryDTOs = agreement.getHelpCategories().stream()
                    .map(AgreementHelpCategoryDTO::from)
                    .toList();

            return new Result(
                    agreement.getId(),
                    agreement.getStatus(),
                    agreement.getConfirmationDate(),
                    agreement.getType(),
                    agreement.getIsVolunteer(),
                    categoryDTOs,
                    agreement.getUnitHoney(),
                    agreement.getTotalHoney(),
                    agreement.getRegion(),
                    agreement.getIsDayComplete(),
                    agreement.getIsTermComplete()
            );
        }
    }
}
