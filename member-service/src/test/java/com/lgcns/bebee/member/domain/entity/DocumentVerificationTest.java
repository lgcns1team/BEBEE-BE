package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * DocumentVerification Entity Unit Test
 * 
 * 테스트 범위: 엔티티 비즈니스 로직 (생성, 상태 변경 등)
 * Mock 대상: 없음 (순수 도메인 로직)
 */
@DisplayName("DocumentVerification 엔티티 단위 테스트")
class DocumentVerificationTest {

    private Document testDocument;

    @BeforeEach
    void setUp() {
        testDocument = org.springframework.beans.BeanUtils.instantiateClass(Document.class);
        ReflectionTestUtils.setField(testDocument, "documentId", 100L);
        ReflectionTestUtils.setField(testDocument, "targetRole", "HELPER");
        ReflectionTestUtils.setField(testDocument, "docCode", "DOC001");
        ReflectionTestUtils.setField(testDocument, "docNameKo", "자격증");
        ReflectionTestUtils.setField(testDocument, "description", "테스트 설명");
    }

    @Nested
    @DisplayName("of() 정적 팩토리 메서드")
    class OfTest {

        @Test
        @DisplayName("올바른 파라미터로 생성하면 PENDING 상태의 DocumentVerification이 생성된다")
        void of_withValidParams_createsPendingVerification() {
            // given
            String fileUrl = "http://test.com/documents/file.jpg";

            // when
            DocumentVerification verification = DocumentVerification.of(fileUrl, testDocument);

            // then
            assertThat(verification).isNotNull();
            assertThat(verification.getFileUrl()).isEqualTo(fileUrl);
            assertThat(verification.getDocument()).isEqualTo(testDocument);
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.PENDING);
        }

        @Test
        @DisplayName("생성 시 분석 점수는 null이다")
        void of_createsWithNullScores() {
            // given
            String fileUrl = "http://test.com/documents/file.jpg";

            // when
            DocumentVerification verification = DocumentVerification.of(fileUrl, testDocument);

            // then
            assertThat(verification.getExifScore()).isNull();
            assertThat(verification.getOcrScore()).isNull();
            assertThat(verification.getForgeryScore()).isNull();
            assertThat(verification.getSystemFlag()).isNull();
        }
    }

    @Nested
    @DisplayName("applyAnalysisResult() 메서드")
    class ApplyAnalysisResultTest {

        @Test
        @DisplayName("분석 결과를 적용하면 점수와 플래그가 설정된다")
        void applyAnalysisResult_setsScoresAndFlag() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            Integer exifScore = 85;
            Integer ocrScore = 70;
            Integer forgeryScore = 76;
            String systemFlag = "MID";

            // when
            verification.applyAnalysisResult(exifScore, ocrScore, forgeryScore, systemFlag);

            // then
            assertThat(verification.getExifScore()).isEqualTo(exifScore);
            assertThat(verification.getOcrScore()).isEqualTo(ocrScore);
            assertThat(verification.getForgeryScore()).isEqualTo(forgeryScore);
            assertThat(verification.getSystemFlag()).isEqualTo(systemFlag);
        }

        @Test
        @DisplayName("분석 결과 적용 시 상태는 변경되지 않는다")
        void applyAnalysisResult_doesNotChangeStatus() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            DocumentStatus initialStatus = verification.getStatus();

            // when
            verification.applyAnalysisResult(80, 75, 77, "LOW");

            // then
            assertThat(verification.getStatus()).isEqualTo(initialStatus);
        }
    }

    @Nested
    @DisplayName("approve() 메서드")
    class ApproveTest {

        @Test
        @DisplayName("승인하면 상태가 APPROVED로 변경된다")
        void approve_changesStatusToApproved() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.PENDING);

            // when
            verification.approve();

            // then
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.APPROVED);
        }

        @Test
        @DisplayName("승인 시 reason은 null로 유지된다")
        void approve_keepsReasonNull() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);

            // when
            verification.approve();

            // then
            assertThat(verification.getReason()).isNull();
        }
    }

    @Nested
    @DisplayName("reject() 메서드")
    class RejectTest {

        @Test
        @DisplayName("거절하면 상태가 REJECTED로 변경된다")
        void reject_changesStatusToRejected() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            String reason = "위변조가 의심됩니다.";

            // when
            verification.reject(reason);

            // then
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.REJECTED);
        }

        @Test
        @DisplayName("거절하면 거절 사유가 저장된다")
        void reject_savesReason() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            String reason = "위변조가 의심됩니다.";

            // when
            verification.reject(reason);

            // then
            assertThat(verification.getReason()).isEqualTo(reason);
        }

        @Test
        @DisplayName("여러 번 거절하면 마지막 사유로 덮어쓴다")
        void reject_multipleTimesOverwritesReason() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            String firstReason = "첫 번째 사유";
            String secondReason = "두 번째 사유";

            // when
            verification.reject(firstReason);
            verification.reject(secondReason);

            // then
            assertThat(verification.getReason()).isEqualTo(secondReason);
        }
    }

    @Nested
    @DisplayName("상태 전이 테스트")
    class StatusTransitionTest {

        @Test
        @DisplayName("PENDING → APPROVED 전이가 가능하다")
        void pendingToApproved_isPossible() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.PENDING);

            // when
            verification.approve();

            // then
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.APPROVED);
        }

        @Test
        @DisplayName("PENDING → REJECTED 전이가 가능하다")
        void pendingToRejected_isPossible() {
            // given
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", testDocument);
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.PENDING);

            // when
            verification.reject("거절 사유");

            // then
            assertThat(verification.getStatus()).isEqualTo(DocumentStatus.REJECTED);
        }
    }
}

