package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.payment.common.exception.PaymentInvalidParamErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyEscrow;
import com.lgcns.bebee.payment.domain.entity.HoneyHistory;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import com.lgcns.bebee.payment.domain.entity.vo.HoneyHistoryType;
import com.lgcns.bebee.payment.domain.repository.HoneyEscrowRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyHistoryRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import com.lgcns.bebee.payment.domain.service.HoneyEscrowService;
import com.lgcns.bebee.payment.domain.service.HoneyWalletService;

import com.lgcns.bebee.payment.domain.service.MatchReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrantHelperHoneyUseCase implements UseCase<GrantHelperHoneyUseCase.Param, Void> {

    private final HoneyWalletService honeyWalletService;
    private final HoneyWalletRepository honeyWalletRepository;
    private final HoneyHistoryRepository honeyHistoryRepository;
    private final HoneyEscrowRepository honeyEscrowRepository;
    private final HoneyEscrowService honeyEscrowService;
    private final MatchReader matchReader;

    @Override
    @Transactional
    public Void execute(Param param) {
        param.validate();

        PaymentMatchSync match = matchReader.findById(param.getMatchId());
        Long helperId = match.getHelperId();

        Long honeyToGrant = match.getAgreement().getUnitHoney(); // 다른 브랜치 중에서 honey 타입 다 Long으로 바꾼거 합쳐지면 오류 자동 수정

        // 꿀 보관소에서 꺼내온다
        HoneyEscrow escrow = honeyEscrowService.findByMatch_MatchId(param.getMatchId());
        escrow.transfer(honeyToGrant);
        honeyEscrowRepository.save(escrow);

        // 도우미 지갑에 쌓인다
        HoneyWallet wallet = honeyWalletService.findByMemberId(helperId);
        wallet.charge(honeyToGrant * 100);
        HoneyWallet savedWallet = honeyWalletRepository.saveAndFlush(wallet);

        HoneyHistory history = HoneyHistory.create(
                savedWallet,
                helperId,
                honeyToGrant,
                HoneyHistoryType.EARNING
        );
        honeyHistoryRepository.save(history);

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long matchId;

        public static Boolean validate(Param param) {
            if (!ParamValidator.isValidId(param.matchId)) {
                throw new InvalidParamException(PaymentInvalidParamErrors.REQUIRED_FIELD, "matchId");
            }

            return true;
        }
    }
}
