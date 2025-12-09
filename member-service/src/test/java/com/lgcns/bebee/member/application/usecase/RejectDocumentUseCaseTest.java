package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.member.common.exception.DocumentException;
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
 * RejectDocumentUseCase Unit Test
 * 
 * 테스트 범위: 문서 거절 유스케이스 로직
 * Mock 대상: DocumentManagement
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RejectDocumentUseCase 단위 테스트")
class RejectDocumentUseCaseTest {

    @Mock
    private DocumentManagement documentManagement;

    @InjectMocks
    private RejectDocumentUseCase rejectDocumentUseCase;

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
        @DisplayName("정상적인 파라미터로 실행하면 거절 처리된다")
        void execute_withValidParams_rejectsDocument() {
            // given
            Long verificationId = 1L;
            String reason = "위변조가 의심됩니다.";
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(verificationId, reason);

            given(documentManagement.load(verificationId)).willReturn(testVerification);
            willDoNothing().given(documentManagement).reject(testVerification, reason);

            // when
            Void result = rejectDocumentUseCase.execute(param);

            // then
            assertThat(result).isNull();
            then(documentManagement).should().load(verificationId);
            then(documentManagement).should().reject(testVerification, reason);
        }

        @Test
        @DisplayName("존재하지 않는 검증 ID로 실행하면 예외가 발생한다")
        void execute_withNonExistingId_throwsException() {
            // given
            Long verificationId = 999L;
            String reason = "테스트 사유";
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(verificationId, reason);

            given(documentManagement.load(verificationId))
                    .willThrow(new DocumentException(
                            com.lgcns.bebee.member.common.exception.DocumentErrors.NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> rejectDocumentUseCase.execute(param))
                    .isInstanceOf(DocumentException.class);
        }

        @Test
        @DisplayName("이미 처리된 문서를 거절하려고 하면 예외가 발생한다")
        void execute_withAlreadyProcessedDocument_throwsException() {
            // given
            Long verificationId = 1L;
            String reason = "테스트 사유";
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(verificationId, reason);

            given(documentManagement.load(verificationId)).willReturn(testVerification);
            willThrow(new DocumentException(
                    com.lgcns.bebee.member.common.exception.DocumentErrors.ALREADY_PROCESSED))
                    .given(documentManagement).reject(testVerification, reason);

            // when & then
            assertThatThrownBy(() -> rejectDocumentUseCase.execute(param))
                    .isInstanceOf(DocumentException.class);
        }
    }

    @Nested
    @DisplayName("Param 검증")
    class ParamValidationTest {

        @Test
        @DisplayName("verificationId가 null이면 예외가 발생한다")
        void param_withNullVerificationId_throwsException() {
            // given
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(null, "사유");

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("검증 ID는 필수입니다.");
        }

        @Test
        @DisplayName("reason이 null이면 예외가 발생한다")
        void param_withNullReason_throwsException() {
            // given
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(1L, null);

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("거절 사유는 필수입니다.");
        }

        @Test
        @DisplayName("reason이 빈 문자열이면 예외가 발생한다")
        void param_withBlankReason_throwsException() {
            // given
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(1L, "   ");

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("거절 사유는 필수입니다.");
        }

        @Test
        @DisplayName("모든 파라미터가 유효하면 true를 반환한다")
        void param_withValidParams_returnsTrue() {
            // given
            RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(1L, "거절 사유입니다.");

            // when
            boolean result = param.validate();

            // then
            assertThat(result).isTrue();
        }
    }
}

