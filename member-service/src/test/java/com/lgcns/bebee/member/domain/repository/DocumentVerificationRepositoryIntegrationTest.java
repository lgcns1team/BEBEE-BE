package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.DocumentStatus;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.infrastructure.security.RefreshTokenBlacklistService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * DocumentVerificationRepository Integration Test
 * 
 * 테스트 범위: DB 연동 테스트 (Repository 계층)
 * 테스트 DB: 실제 MySQL 테스트 프로필
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("DocumentVerificationRepository 통합 테스트")
class DocumentVerificationRepositoryIntegrationTest {

    @Autowired
    private DocumentVerificationRepository documentVerificationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager entityManager;

    @MockBean
    private RefreshTokenBlacklistService refreshTokenBlacklistService;

    @MockBean
    private AuditorAware<?> auditorAware;

    private Member testMember;
    private Document testDocument;

    @BeforeEach
    void setUp() {
        // Member 생성 (Document의 필수 관계)
        testMember = org.springframework.beans.BeanUtils.instantiateClass(Member.class);
        ReflectionTestUtils.setField(testMember, "email", "test@test.com");
        ReflectionTestUtils.setField(testMember, "password", "hashedPassword");
        ReflectionTestUtils.setField(testMember, "phoneNumber", "010-1234-5678");
        ReflectionTestUtils.setField(testMember, "name", "테스트유저");
        ReflectionTestUtils.setField(testMember, "nickname", "테스터");
        ReflectionTestUtils.setField(testMember, "birthDate", java.time.LocalDate.of(1990, 1, 1));
        ReflectionTestUtils.setField(testMember, "gender", Gender.NONE);
        ReflectionTestUtils.setField(testMember, "role", Role.HELPER);
        ReflectionTestUtils.setField(testMember, "status", MemberStatus.ACTIVE);
        ReflectionTestUtils.setField(testMember, "latitude", java.math.BigDecimal.valueOf(37.5665));
        ReflectionTestUtils.setField(testMember, "longitude", java.math.BigDecimal.valueOf(126.9780));
        ReflectionTestUtils.setField(testMember, "districtCode", "11010");
        ReflectionTestUtils.setField(testMember, "sweetness", java.math.BigDecimal.valueOf(40.00));
        entityManager.persist(testMember);

        // Document 생성
        testDocument = org.springframework.beans.BeanUtils.instantiateClass(Document.class);
        ReflectionTestUtils.setField(testDocument, "targetRole", "HELPER");
        ReflectionTestUtils.setField(testDocument, "docCode", "DOC001");
        ReflectionTestUtils.setField(testDocument, "docNameKo", "자격증");
        ReflectionTestUtils.setField(testDocument, "description", "테스트 설명");
        ReflectionTestUtils.setField(testDocument, "member", testMember);
        entityManager.persist(testDocument);

        entityManager.flush();
        entityManager.clear();
    }

    @Nested
    @DisplayName("save() 메서드")
    class SaveTest {

        @Test
        @DisplayName("DocumentVerification을 저장하면 ID가 생성된다")
        void save_generatesId() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", doc);
            verification.applyAnalysisResult(80, 75, 77, "LOW");

            // when
            DocumentVerification saved = documentVerificationRepository.save(verification);
            entityManager.flush();

            // then
            assertThat(saved.getDocumentVerificationId()).isNotNull();
        }

        @Test
        @DisplayName("저장된 DocumentVerification의 모든 필드가 정확히 저장된다")
        void save_persistsAllFields() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());
            String fileUrl = "http://test.com/documents/important.jpg";
            DocumentVerification verification = DocumentVerification.of(fileUrl, doc);
            verification.applyAnalysisResult(85, 70, 76, "MID");

            // when
            DocumentVerification saved = documentVerificationRepository.save(verification);
            entityManager.flush();
            entityManager.clear();

            // then
            DocumentVerification found = entityManager.find(DocumentVerification.class, saved.getDocumentVerificationId());
            assertThat(found.getFileUrl()).isEqualTo(fileUrl);
            assertThat(found.getExifScore()).isEqualTo(85);
            assertThat(found.getOcrScore()).isEqualTo(70);
            assertThat(found.getForgeryScore()).isEqualTo(76);
            assertThat(found.getSystemFlag()).isEqualTo("MID");
            assertThat(found.getStatus()).isEqualTo(DocumentStatus.PENDING);
        }
    }

    @Nested
    @DisplayName("findById() 메서드")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 DocumentVerification을 반환한다")
        void findById_withExistingId_returnsVerification() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", doc);
            entityManager.persist(verification);
            entityManager.flush();
            entityManager.clear();

            // when
            Optional<DocumentVerification> found = documentVerificationRepository.findById(
                    verification.getDocumentVerificationId()
            );

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getFileUrl()).isEqualTo("http://test.com/file.jpg");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환한다")
        void findById_withNonExistingId_returnsEmpty() {
            // when
            Optional<DocumentVerification> found = documentVerificationRepository.findById(999999L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByStatus() 메서드")
    class FindByStatusTest {

        @Test
        @DisplayName("PENDING 상태의 문서들만 조회한다")
        void findByStatus_returnsPendingOnly() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());

            // PENDING 상태 2개
            DocumentVerification pending1 = DocumentVerification.of("http://test.com/pending1.jpg", doc);
            DocumentVerification pending2 = DocumentVerification.of("http://test.com/pending2.jpg", doc);
            entityManager.persist(pending1);
            entityManager.persist(pending2);

            // APPROVED 상태 1개
            DocumentVerification approved = DocumentVerification.of("http://test.com/approved.jpg", doc);
            approved.approve();
            entityManager.persist(approved);

            // REJECTED 상태 1개
            DocumentVerification rejected = DocumentVerification.of("http://test.com/rejected.jpg", doc);
            rejected.reject("거절 사유");
            entityManager.persist(rejected);

            entityManager.flush();
            entityManager.clear();

            // when
            List<DocumentVerification> pendingList = documentVerificationRepository.findByStatus(DocumentStatus.PENDING);

            // then
            assertThat(pendingList).hasSize(2);
            assertThat(pendingList).allMatch(v -> v.getStatus() == DocumentStatus.PENDING);
        }

        @Test
        @DisplayName("APPROVED 상태의 문서들만 조회한다")
        void findByStatus_returnsApprovedOnly() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());

            DocumentVerification pending = DocumentVerification.of("http://test.com/pending.jpg", doc);
            entityManager.persist(pending);

            DocumentVerification approved1 = DocumentVerification.of("http://test.com/approved1.jpg", doc);
            approved1.approve();
            entityManager.persist(approved1);

            DocumentVerification approved2 = DocumentVerification.of("http://test.com/approved2.jpg", doc);
            approved2.approve();
            entityManager.persist(approved2);

            entityManager.flush();
            entityManager.clear();

            // when
            List<DocumentVerification> approvedList = documentVerificationRepository.findByStatus(DocumentStatus.APPROVED);

            // then
            assertThat(approvedList).hasSize(2);
            assertThat(approvedList).allMatch(v -> v.getStatus() == DocumentStatus.APPROVED);
        }

        @Test
        @DisplayName("해당 상태의 문서가 없으면 빈 리스트를 반환한다")
        void findByStatus_withNoMatching_returnsEmptyList() {
            // given - 아무 것도 저장하지 않음

            // when
            List<DocumentVerification> result = documentVerificationRepository.findByStatus(DocumentStatus.APPROVED);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("상태 업데이트 테스트")
    class StatusUpdateTest {

        @Test
        @DisplayName("PENDING에서 APPROVED로 상태 변경이 DB에 반영된다")
        void approve_persistsToDatabase() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", doc);
            entityManager.persist(verification);
            entityManager.flush();
            Long id = verification.getDocumentVerificationId();
            entityManager.clear();

            // when
            DocumentVerification found = documentVerificationRepository.findById(id).orElseThrow();
            found.approve();
            documentVerificationRepository.save(found);
            entityManager.flush();
            entityManager.clear();

            // then
            DocumentVerification result = entityManager.find(DocumentVerification.class, id);
            assertThat(result.getStatus()).isEqualTo(DocumentStatus.APPROVED);
        }

        @Test
        @DisplayName("PENDING에서 REJECTED로 상태 변경이 DB에 반영된다")
        void reject_persistsToDatabase() {
            // given
            Document doc = entityManager.find(Document.class, testDocument.getDocumentId());
            DocumentVerification verification = DocumentVerification.of("http://test.com/file.jpg", doc);
            entityManager.persist(verification);
            entityManager.flush();
            Long id = verification.getDocumentVerificationId();
            entityManager.clear();

            // when
            DocumentVerification found = documentVerificationRepository.findById(id).orElseThrow();
            String reason = "위변조가 의심됩니다.";
            found.reject(reason);
            documentVerificationRepository.save(found);
            entityManager.flush();
            entityManager.clear();

            // then
            DocumentVerification result = entityManager.find(DocumentVerification.class, id);
            assertThat(result.getStatus()).isEqualTo(DocumentStatus.REJECTED);
            assertThat(result.getReason()).isEqualTo(reason);
        }
    }
}

