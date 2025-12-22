package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.domain.service.MatchReader;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementHelpCategory;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.exception.MatchErrors;
import com.lgcns.bebee.match.exception.MatchInvalidParamErrors;
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
        // 1. 파라미터 검증
        param.validate();

        // 2-1. Match 조회
        Match match = matchReader.getById(param.getMatchId());

        // 2-2. 작성자가 해당 매칭의 참여자인지 확인
        if (!match.isParticipant(param.getMemberId())) {
            throw MatchErrors.FORBIDDEN.toException();
        }

        // 3. 매칭 확인서 생성
        Agreement agreement = Agreement.create(
                param.getMatchId(),
                param.getType(),
                param.getIsVolunteer(),
                param.getUnitHoney(),
                param.getTotalHoney(),
                param.getRegion(),
                param.getHelpCategoryIds()
        );

        // 4. 매칭 확인서 저장
        Agreement savedAgreement = agreementRepository.save(agreement);

        return Result.from(savedAgreement);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long matchId;
        private final EngagementType type;
        private final Boolean isVolunteer;
        private final Integer unitHoney;
        private final Integer totalHoney;
        private final String region;
        private final List<Long> helpCategoryIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(matchId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.INVALID_FORMAT, "matchId");
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
            if (!ParamValidator.isNotNull(type)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "type");
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
        private Long matchId;
        private AgreementStatus status;
        private LocalDate confirmationDate;
        private EngagementType type;
        private Boolean isVolunteer;
        private List<AgreementHelpCategory> helpCategories;
        private Integer unitHoney;
        private Integer totalHoney;
        private String region;
        private Boolean isDayComplete;
        private Boolean isTermComplete;

        public static Result from(Agreement agreement) {
            return new Result(
                    agreement.getId(),
                    agreement.getMatchId(),
                    agreement.getStatus(),
                    agreement.getConfirmationDate(),
                    agreement.getType(),
                    agreement.getIsVolunteer(),
                    agreement.getHelpCategories(),
                    agreement.getUnitHoney(),
                    agreement.getTotalHoney(),
                    agreement.getRegion(),
                    agreement.getIsDayComplete(),
                    agreement.getIsTermComplete()
            );
        }
    }
}
