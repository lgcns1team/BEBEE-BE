package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.member.core.exception.DocumentException;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * ApproveDocumentUseCase Unit Test
 * 
 * 테스트 범위: 문서 승인 유스케이스 로직
 * Mock 대상: DocumentManagement
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ApproveDocumentUseCase 단위 테스트")
class ApproveDocumentUseCaseTest {

    @Mock
    private DocumentManagement documentManagement;

    @InjectMocks
    private ApproveDocumentUseCase approveDocumentUseCase;

    private DocumentVerification testVerification;

    @BeforeEach
    void setUp() {
        Document document = org.springframework.beans.BeanUtils.instantiateClass(Document.class);
        ReflectionTestUtils.setField(document, "documentId", 100L);
        ReflectionTestUtils.setField(document, "targetRole", "HELPER");
        ReflectionTestUtils.setField(document, "docCode", "DOC001");
        ReflectionTestUtils.setField(document, "docNameKo", "자격증");
        ReflectionTestUtils.setField(document, "description", "테스트 설명");

        testVerification = DocumentVerification.of("http://test.com/file.jpg", document);
        ReflectionTestUtils.setField(testVerification, "documentVerificationId", 1L);
    }

    @Nested
    @DisplayName("execute() 메서드")
    class ExecuteTest {

        @Test
        @DisplayName("정상적인 검증 ID로 실행하면 승인 처리된다")
        void execute_withValidVerificationId_approvesDocument() {
            // given
            Long verificationId = 1L;
            ApproveDocumentUseCase.Param param = new ApproveDocumentUseCase.Param(verificationId);

            given(documentManagement.load(verificationId)).willReturn(testVerification);
            willDoNothing().given(documentManagement).approve(testVerification);

            // when
            Void result = approveDocumentUseCase.execute(param);

            // then
            assertThat(result).isNull();
            then(documentManagement).should().load(verificationId);
            then(documentManagement).should().approve(testVerification);
        }

        @Test
        @DisplayName("존재하지 않는 검증 ID로 실행하면 예외가 발생한다")
        void execute_withNonExistingId_throwsException() {
            // given
            Long verificationId = 999L;
            ApproveDocumentUseCase.Param param = new ApproveDocumentUseCase.Param(verificationId);

            given(documentManagement.load(verificationId))
                    .willThrow(new DocumentException(
                            com.lgcns.bebee.member.core.exception.DocumentErrors.NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> approveDocumentUseCase.execute(param))
                    .isInstanceOf(DocumentException.class);
        }

        @Test
        @DisplayName("이미 처리된 문서를 승인하려고 하면 예외가 발생한다")
        void execute_withAlreadyProcessedDocument_throwsException() {
            // given
            Long verificationId = 1L;
            ApproveDocumentUseCase.Param param = new ApproveDocumentUseCase.Param(verificationId);

            given(documentManagement.load(verificationId)).willReturn(testVerification);
            willThrow(new DocumentException(
                    com.lgcns.bebee.member.core.exception.DocumentErrors.ALREADY_PROCESSED))
                    .given(documentManagement).approve(testVerification);

            // when & then
            assertThatThrownBy(() -> approveDocumentUseCase.execute(param))
                    .isInstanceOf(DocumentException.class);
        }
    }

}

