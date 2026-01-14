package com.lgcns.bebee.member.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.payment.HoneyWalletChangedEvent;
import com.lgcns.bebee.member.domain.entity.sync.MemberHoneyWalletSync;
import com.lgcns.bebee.member.domain.repository.HoneyWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HoneyWalletChangedEventHandler implements EventHandler<HoneyWalletChangedEvent> {
    private final HoneyWalletRepository honeyWalletRepository;

    @Override
    public Class<HoneyWalletChangedEvent> getEventClass() {
        return HoneyWalletChangedEvent.class;
    }

    @Override
    @Transactional
    public void handle(HoneyWalletChangedEvent event) {
        log.info("HoneyWalletChanged 이벤트 처리 시작 - memberId: {}, honeyWalletId: {}, balance: {}",
                event.getMemberId(), event.getHoneyWalletId(), event.getBalance());

        // HoneyWallet 동기화: 있으면 업데이트, 없으면 생성
        MemberHoneyWalletSync wallet = honeyWalletRepository.findByMemberId(event.getMemberId())
                .orElseGet(() -> {
                    log.info("MemberHoneyWalletSync 생성 - memberId: {}", event.getMemberId());
                    return MemberHoneyWalletSync.create(
                            event.getHoneyWalletId(),
                            event.getMemberId(),
                            event.getBalance()
                    );
                });

        wallet.updateBalance(event.getBalance());
        honeyWalletRepository.save(wallet);

        log.info("HoneyWalletChanged 이벤트 처리 완료 - memberId: {}, balance: {}",
                event.getMemberId(), event.getBalance());
    }
}