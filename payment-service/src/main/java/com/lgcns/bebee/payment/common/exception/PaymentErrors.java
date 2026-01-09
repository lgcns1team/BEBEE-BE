package com.lgcns.bebee.payment.common.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;

public enum PaymentErrors implements ErrorInfo {
    PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없습니다."),
    PAYMENT_ALREADY_EXISTS("이미 처리된 결제입니다."),
    PAYMENT_ALREADY_CANCELED("이미 취소된 결제입니다."),
    PAYMENT_CANNOT_CANCEL("취소할 수 없는 결제 상태입니다."),
    PAYMENT_AMOUNT_MISMATCH("결제 금액이 일치하지 않습니다."),
    PAYMENT_MEMBER_MISMATCH("결제 회원 정보가 일치하지 않습니다."),
    INVALID_PAYMENT_AMOUNT("잘못된 결제 금액입니다."),

    HONEY_WALLET_NOT_FOUND("허니 지갑을 찾을 수 없습니다."),
    INSUFFICIENT_HONEY_BALANCE("허니 잔액이 부족합니다."),

    ESCROW_ALREADY_EXISTS("이미 해당 매칭에 대한 꿀 보관소가 존재합니다."),
    ESCROW_NOT_FOUND("꿀 보관소를 찾을 수 없습니다."),

    MATCH_NOT_FOUND("매칭 정보를 찾을 수 없습니다."),
    AGREEMENT_NOT_FOUND("매칭 확인서 정보를 찾을 수 없습니다."),

    TOSS_API_ERROR("토스페이먼츠 API 호출에 실패했습니다."),
    TOSS_API_TIMEOUT("토스페이먼츠 API 응답 시간 초과"),

    PAYMENT_ACCESS_DENIED("결제 정보에 접근할 권한이 없습니다.");

    private final String desc;

    PaymentErrors(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public DomainException toException() {
        return new PaymentException(this);
    }
}
