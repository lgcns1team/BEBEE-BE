package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.presentation.dto.DayEngagementTimeDTO;
import com.lgcns.bebee.match.presentation.dto.TermEngagementTimeDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementHelpCategoryDTO;
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
    private final MemberManager memberManager;

    @Transactional
    @Override
    public Result execute(Param param) {
        // 파라미터 검증
        param.validate();

        // 생성하려는 사용자 검증, 장애인 유저인지 확인
        MemberSync member = memberManager.findExistingMember(param.getDisabledId());

        if (member.getRole() != Role.DISABLED) {
            throw MatchErrors.ONLY_DISABLED_MEMBERS_ALLOWED.toException();
        }

        // 이미 성사된 매칭이면 새로 생성 불가
        agreementRepository.findByPostId(param.getPostId())
                .ifPresent(existingAgreement -> {
                    if (existingAgreement.getStatus() == AgreementStatus.CONFIRMED) {
                        throw MatchErrors.ALREADY_MATCHED.toException();
                    }
                });

        // 매칭 확인서 생성
        Agreement agreement = Agreement.create(
                param.getPostId(),
                param.getHelperId(),
                param.getDisabledId(),
                param.getType(),
                param.getIsVolunteer(),
                param.getUnitHoney(),
                param.getTotalHoney(),
                param.getRegion(),
                param.getDayEngagementTime(),
                param.getTermEngagementTime(),
                param.getHelpCategoryIds()
        );

        // 매칭 확인서 저장
        Agreement savedAgreement = agreementRepository.save(agreement);

        return Result.from(savedAgreement);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long postId;
        private final Long helperId;
        private final Long disabledId;
        private final EngagementType type;
        private final Boolean isVolunteer;
        private final Integer unitHoney;
        private final Integer totalHoney;
        private final String region;
        private final DayEngagementTimeDTO dayEngagementTime;
        private final TermEngagementTimeDTO termEngagementTime;
        private final List<Long> helpCategoryIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(postId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "postId");
            }
            if (!ParamValidator.isValidId(helperId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "helperId");
            }
            if (!ParamValidator.isValidId(disabledId)) {
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
        private Object engagementTime;
        private Boolean isDayComplete;
        private Boolean isTermComplete;

        public static Result from(Agreement agreement) {
            Object engagementTime = null;

            if (agreement.getType() == EngagementType.DAY && agreement.getPeriod() != null && !agreement.getSchedules().isEmpty()) {
                engagementTime = DayEngagementTimeDTO.from(agreement.getPeriod(), agreement.getSchedules().get(0));
            } else if (agreement.getType() == EngagementType.TERM && agreement.getPeriod() != null) {
                engagementTime = TermEngagementTimeDTO.from(agreement.getPeriod(), agreement.getSchedules());
            }

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
                    engagementTime,
                    agreement.getIsDayComplete(),
                    agreement.getIsTermComplete()
            );
        }
    }
}
