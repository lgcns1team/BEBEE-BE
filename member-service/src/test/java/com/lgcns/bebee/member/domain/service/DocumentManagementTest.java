package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.core.exception.DocumentException;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * DocumentManagement Unit Test
 * 
 * 테스트 범위: 문서 검증 도메인 서비스 로직
 * Mock 대상: DocumentVerificationRepository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DocumentManagement 단위 테스트")
class DocumentManagementTest {

    @Mock
    private DocumentVerificationRepository documentVerificationRepository;

    @InjectMocks
    private DocumentManagement documentManagement;

    private DocumentVerification createTestVerification(DocumentStatus status) {
        Document document = createTestDocument();
        DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", document);
        ReflectionTestUtils.setField(verification, "documentVerificationId", 1L);
        ReflectionTestUtils.setField(verification, "status", status);
        return verification;
    }

    private Document createTestDocument() {
        Document document = org.springframework.beans.BeanUtils.instantiateClass(Document.class);
        ReflectionTestUtils.setField(document, "documentId", 100L);
        ReflectionTestUtils.setField(document, "targetRole", "HELPER");
        ReflectionTestUtils.setField(document, "docCode", "DOC001");
        ReflectionTestUtils.setField(document, "docNameKo", "자격증");
        ReflectionTestUtils.setField(document, "description", "테스트 설명");
        return document;
    }

    @Nested
    @DisplayName("load() 메서드")
    class LoadTest {

        @Test
        @DisplayName("존재하는 검증 ID로 조회하면 DocumentVerification을 반환한다")
        void load_withExistingId_returnsVerification() {
            // given
            Long verificationId = 1L;
            DocumentVerification verification = createTestVerification(DocumentStatus.PENDING);
            given(documentVerificationRepository.findById(verificationId))
                    .willReturn(Optional.of(verification));

            // when
            DocumentVerification result = documentManagement.load(verificationId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getDocumentVerificationId()).isEqualTo(verificationId);
            then(documentVerificationRepository).should().findById(verificationId);
        }

        @Test
        @DisplayName("존재하지 않는 검증 ID로 조회하면 예외가 발생한다")
        void load_withNonExistingId_throwsException() {
            // given
            Long verificationId = 999L;
            given(documentVerificationRepository.findById(verificationId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> documentManagement.load(verificationId))
                    .isInstanceOf(DocumentException.class);
        }
    }

    @Nested
    @DisplayName("loadPendingList() 메서드")
    class LoadPendingListTest {

        @Test
        @DisplayName("PENDING 상태의 문서 검증 목록을 반환한다")
        void loadPendingList_returnsPendingVerifications() {
            // given
            DocumentVerification verification1 = createTestVerification(DocumentStatus.PENDING);
            DocumentVerification verification2 = createTestVerification(DocumentStatus.PENDING);
            ReflectionTestUtils.setField(verification2, "documentVerificationId", 2L);

            given(documentVerificationRepository.findByStatus(DocumentStatus.PENDING))
                    .willReturn(List.of(verification1, verification2));

            // when
            List<DocumentVerification> result = documentManagement.loadPendingList();

            // then
            assertThat(result).hasSize(2);
            then(documentVerificationRepository).should().findByStatus(DocumentStatus.PENDING);
        }

        @Test
        @DisplayName("PENDING 상태의 문서가 없으면 빈 리스트를 반환한다")
        void loadPendingList_withNoPending_returnsEmptyList() {
            // given
            given(documentVerificationRepository.findByStatus(DocumentStatus.PENDING))
                    .willReturn(List.of());

            // when
            List<DocumentVerification> result = documentManagement.loadPendingList();

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("approve() 메서드")
    class ApproveTest {

        @Test
        @DisplayName("PENDING 상태의 문서를 승인하면 상태가 APPROVED로 변경된다")
        void approve_withPendingStatus_changesStatusToApproved() {
            // given
            DocumentVerification verification = createTestVerification(DocumentStatus.PENDING);

            // when
            documentManagement.approve(verification);

            // then
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.APPROVED);
        }

        @Test
        @DisplayName("이미 처리된 문서를 승인하려고 하면 예외가 발생한다")
        void approve_withAlreadyProcessed_throwsException() {
            // given
            DocumentVerification verification = createTestVerification(DocumentStatus.APPROVED);

            // when & then
            assertThatThrownBy(() -> documentManagement.approve(verification))
                    .isInstanceOf(DocumentException.class);
        }

        @Test
        @DisplayName("REJECTED 상태의 문서를 승인하려고 하면 예외가 발생한다")
        void approve_withRejectedStatus_throwsException() {
            // given
            DocumentVerification verification = createTestVerification(DocumentStatus.REJECTED);

            // when & then
            assertThatThrownBy(() -> documentManagement.approve(verification))
                    .isInstanceOf(DocumentException.class);
        }
    }

    @Nested
    @DisplayName("reject() 메서드")
    class RejectTest {

        @Test
        @DisplayName("PENDING 상태의 문서를 거절하면 상태가 REJECTED로 변경되고 사유가 저장된다")
        void reject_withPendingStatus_changesStatusToRejectedWithReason() {
            // given
            DocumentVerification verification = createTestVerification(DocumentStatus.PENDING);
            String reason = "위변조가 의심됩니다.";

            // when
            documentManagement.reject(verification, reason);

            // then
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.REJECTED);
            assertThat(verification.getReason()).isEqualTo(reason);
        }

        @Test
        @DisplayName("이미 처리된 문서를 거절하려고 하면 예외가 발생한다")
        void reject_withAlreadyProcessed_throwsException() {
            // given
            DocumentVerification verification = createTestVerification(DocumentStatus.APPROVED);
            String reason = "테스트 사유";

            // when & then
            assertThatThrownBy(() -> documentManagement.reject(verification, reason))
                    .isInstanceOf(DocumentException.class);
        }

        @Test
        @DisplayName("REJECTED 상태의 문서를 다시 거절하려고 하면 예외가 발생한다")
        void reject_withRejectedStatus_throwsException() {
            // given
            DocumentVerification verification = createTestVerification(DocumentStatus.REJECTED);
            String reason = "다른 사유";

            // when & then
            assertThatThrownBy(() -> documentManagement.reject(verification, reason))
                    .isInstanceOf(DocumentException.class);
        }
    }
}

