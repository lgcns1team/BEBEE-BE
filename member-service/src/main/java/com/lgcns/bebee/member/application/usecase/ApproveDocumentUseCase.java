package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 문서 승인 유스케이스
 * 관리자가 문서를 승인 처리
 */
@Service
@RequiredArgsConstructor
public class ApproveDocumentUseCase implements UseCase<ApproveDocumentUseCase.Param, Void> {

    private final DocumentManagement documentManagement;

    /**
     * 문서 승인 실행
     * @param param 승인 파라미터
     * @return null (Void)
     */
    @Override
    @Transactional
    public Void execute(Param param) {
        // 파라미터 검증
        param.validate();

        // 1. 문서 검증 조회 (Domain)
        DocumentVerification verification = documentManagement.load(param.getVerificationId());

        // 2. 승인 처리 (Domain - 비즈니스 규칙 검증 포함)
        documentManagement.approve(verification);

        return null;
    }

    /**
     * 승인 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long verificationId;

        @Override
        public boolean validate() {
            if (verificationId == null) {
                throw new IllegalArgumentException("검증 ID는 필수입니다.");
            }
            return true;
        }
    }
}
