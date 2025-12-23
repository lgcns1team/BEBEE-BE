package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.exception.MatchInvalidParamErrors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmAgreementUseCase implements UseCase<ConfirmAgreementUseCase.Param, ConfirmAgreementUseCase.Result>{

    private final AgreementReader agreementReader;
    private final MatchRepository matchRepository;

    @Transactional
    @Override
    public Result execute(ConfirmAgreementUseCase.Param param) {
        param.validate();

        Agreement agreement = agreementReader.getById(param.getAgreementId());

        agreement.confirm();

        Match match = Match.create(
                param.getHelperId(),
                param.getDisabledId(),
                param.getPostId(),
                param.getTitle(),
                param.getChatRoomId(),
                param.getAgreementId()
        );
        Match savedMatch = matchRepository.save(match);

        return Result.from(savedMatch);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long helperId;
        private final Long disabledId;
        private final Long postId;
        private final String title;
        private final Long chatRoomId;
        private final Long agreementId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(helperId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "helperId");
            }
            if (!ParamValidator.isValidId(disabledId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "disabledId");
            }
            if (!ParamValidator.isValidId(postId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "postId");
            }
            if (!ParamValidator.isValidString(title)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "title");
            }
            if (!ParamValidator.isValidStringLength(title, 1, 100)) {
                throw new InvalidParamException(MatchInvalidParamErrors.OUT_OF_RANGE, "title");
            }
            if (!ParamValidator.isValidId(chatRoomId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "chatRoomId");
            }
            if (!ParamValidator.isValidId(agreementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "agreementId");
            }

            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Long matchId;

        public static Result from(Match match) {
            return new Result(
                    match.getMatchId()
            );
        }
    }

}
