package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementHelpCategory;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.service.AgreementManagement;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.exception.MatchErrors;
import com.lgcns.bebee.match.exception.MatchException;
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
    private final AgreementManagement agreementManagement;
    private final MatchRepository matchRepository;

    @Transactional
    @Override
    public Result execute(Param param) {
        // 1. 파라미터 검증
        param.validate();

        // 2-1. 작성자 확인
        Match match = matchRepository.findById(param.getMatchId())
                                    .orElseThrow(() -> MatchErrors.MATCH_NOT_FOUND.toException());
        // 2-2. 작성자가 해당 매칭의 참여자인지 확인
        if (!match.isParticipant(param.getMemberId())) {
            throw MatchErrors.FORBIDDEN.toException();
        }

        // 3. 매칭 확인서 생성
        Agreement agreement = agreementManagement.createAgreement(
                param.getMatchId(),
                param.getType(),
                param.getIsVolunteer(),
                param.getUnitHoney(),
                param.getTotalHoney(),
                param.getRegion()
        );

        // 4. 도움 카테고리 추가
        param.getHelpCategoryIds().forEach(categoryId -> {
            AgreementHelpCategory category = AgreementHelpCategory.create(categoryId);
            agreement.addHelpCategory(category);
        });

        // 5. 매칭 확인서 저장 (CascadeType.ALL로 helpCategories도 자동 저장)
        Agreement savedAgreement = agreementRepository.save(agreement);

        return Result.from(savedAgreement);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;  // 작성자 확인용
        private final Long matchId;
        private final EngagementType type;
        private final Boolean isVolunteer;
        private final List<Long> helpCategoryIds;
        private final Integer unitHoney;
        private final Integer totalHoney;
        private final String region;

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
        private AgreementInfo agreementInfo;

        public static Result from(Agreement agreement) {
            AgreementInfo info = AgreementInfo.from(agreement);
            return new Result(info);
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class AgreementInfo {
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

            public static AgreementInfo from(Agreement agreement) {
                return new AgreementInfo(
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
}
