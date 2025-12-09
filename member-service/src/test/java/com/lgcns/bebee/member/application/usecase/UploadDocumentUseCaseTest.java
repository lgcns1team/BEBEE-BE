package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import com.lgcns.bebee.member.domain.service.DocumentVerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

/**
 * UploadDocumentUseCase Unit Test
 * 
 * 테스트 범위: 문서 업로드 유스케이스 로직
 * Mock 대상: FileStorageClient, DocumentVerificationService, Repositories
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UploadDocumentUseCase 단위 테스트")
class UploadDocumentUseCaseTest {

    @Mock
    private FileStorageClient fileStorageClient;

    @Mock
    private DocumentVerificationService verificationService;

    @Mock
    private DocumentManagement documentManagement;

    @Mock
    private DocumentVerificationRepository verificationRepository;

    @InjectMocks
    private UploadDocumentUseCase uploadDocumentUseCase;

    private Document testDocument;
    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        testDocument = org.springframework.beans.BeanUtils.instantiateClass(Document.class);
        ReflectionTestUtils.setField(testDocument, "documentId", 100L);
        ReflectionTestUtils.setField(testDocument, "targetRole", "HELPER");
        ReflectionTestUtils.setField(testDocument, "docCode", "DOC001");
        ReflectionTestUtils.setField(testDocument, "docNameKo", "자격증");
        ReflectionTestUtils.setField(testDocument, "description", "테스트 설명");

        testFile = new MockMultipartFile(
                "file",
                "test-document.jpg",
                "image/jpeg",
                new byte[50 * 1024]
        );
    }

    @Nested
    @DisplayName("execute() 메서드")
    class ExecuteTest {

        @Test
        @DisplayName("정상적인 파라미터로 실행하면 검증 ID를 반환한다")
        void execute_withValidParam_returnsVerificationId() {
            // given
            Long memberId = 1L;
            Long documentId = 100L;
            String uploadedFileUrl = "http://storage.test.com/documents/test-document.jpg";

            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    memberId, documentId, testFile
            );

            DocumentVerificationService.AnalysisResult analysisResult =
                    new DocumentVerificationService.AnalysisResult(80, 75, 77, "LOW");

            given(fileStorageClient.upload(any(MultipartFile.class), eq("documents")))
                    .willReturn(uploadedFileUrl);
            given(verificationService.analyze(any(MultipartFile.class)))
                    .willReturn(analysisResult);
            given(documentManagement.loadDocument(documentId))
                    .willReturn(testDocument);
            given(verificationRepository.save(any(DocumentVerification.class)))
                    .willAnswer(invocation -> {
                        DocumentVerification v = invocation.getArgument(0);
                        ReflectionTestUtils.setField(v, "documentVerificationId", 999L);
                        return v;
                    });

            // when
            Long result = uploadDocumentUseCase.execute(param);

            // then
            assertThat(result).isEqualTo(999L);
            then(fileStorageClient).should().upload(testFile, "documents");
            then(verificationService).should().analyze(testFile);
            then(documentManagement).should().loadDocument(documentId);
            then(verificationRepository).should().save(any(DocumentVerification.class));
        }

        @Test
        @DisplayName("파일 업로드 후 분석 결과가 DocumentVerification에 적용된다")
        void execute_appliesAnalysisResultToVerification() {
            // given
            Long memberId = 1L;
            Long documentId = 100L;
            String uploadedFileUrl = "http://storage.test.com/documents/test-document.jpg";

            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    memberId, documentId, testFile
            );

            DocumentVerificationService.AnalysisResult analysisResult =
                    new DocumentVerificationService.AnalysisResult(85, 70, 76, "MID");

            given(fileStorageClient.upload(any(MultipartFile.class), eq("documents")))
                    .willReturn(uploadedFileUrl);
            given(verificationService.analyze(any(MultipartFile.class)))
                    .willReturn(analysisResult);
            given(documentManagement.loadDocument(documentId))
                    .willReturn(testDocument);
            given(verificationRepository.save(any(DocumentVerification.class)))
                    .willAnswer(invocation -> {
                        DocumentVerification v = invocation.getArgument(0);
                        ReflectionTestUtils.setField(v, "documentVerificationId", 999L);
                        return v;
                    });

            // when
            uploadDocumentUseCase.execute(param);

            // then
            ArgumentCaptor<DocumentVerification> captor = ArgumentCaptor.forClass(DocumentVerification.class);
            then(verificationRepository).should().save(captor.capture());

            DocumentVerification saved = captor.getValue();
            assertThat(saved.getFileUrl()).isEqualTo(uploadedFileUrl);
            assertThat(saved.getExifScore()).isEqualTo(85);
            assertThat(saved.getOcrScore()).isEqualTo(70);
            assertThat(saved.getForgeryScore()).isEqualTo(76);
            assertThat(saved.getSystemFlag()).isEqualTo("MID");
        }

        @Test
        @DisplayName("존재하지 않는 문서 ID로 실행하면 예외가 발생한다")
        void execute_withNonExistingDocumentId_throwsException() {
            // given
            Long memberId = 1L;
            Long documentId = 999L;
            String uploadedFileUrl = "http://storage.test.com/documents/test-document.jpg";

            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    memberId, documentId, testFile
            );

            DocumentVerificationService.AnalysisResult analysisResult =
                    new DocumentVerificationService.AnalysisResult(80, 75, 77, "LOW");

            given(fileStorageClient.upload(any(MultipartFile.class), eq("documents")))
                    .willReturn(uploadedFileUrl);
            given(verificationService.analyze(any(MultipartFile.class)))
                    .willReturn(analysisResult);
            given(documentManagement.loadDocument(documentId))
                    .willThrow(new com.lgcns.bebee.member.common.exception.DocumentException(
                            com.lgcns.bebee.member.common.exception.DocumentErrors.DOCUMENT_NOT_FOUND));

            // when & then
            assertThatThrownBy(() -> uploadDocumentUseCase.execute(param))
                    .isInstanceOf(com.lgcns.bebee.member.common.exception.DocumentException.class);
        }
    }

    @Nested
    @DisplayName("Param 검증")
    class ParamValidationTest {

        @Test
        @DisplayName("memberId가 null이면 예외가 발생한다")
        void param_withNullMemberId_throwsException() {
            // given
            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    null, 100L, testFile
            );

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("회원 ID는 필수입니다.");
        }

        @Test
        @DisplayName("documentId가 null이면 예외가 발생한다")
        void param_withNullDocumentId_throwsException() {
            // given
            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    1L, null, testFile
            );

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("문서 ID는 필수입니다.");
        }

        @Test
        @DisplayName("file이 null이면 예외가 발생한다")
        void param_withNullFile_throwsException() {
            // given
            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    1L, 100L, null
            );

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("파일은 필수입니다.");
        }

        @Test
        @DisplayName("file이 비어있으면 예외가 발생한다")
        void param_withEmptyFile_throwsException() {
            // given
            MockMultipartFile emptyFile = new MockMultipartFile(
                    "file", "empty.jpg", "image/jpeg", new byte[0]
            );
            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    1L, 100L, emptyFile
            );

            // when & then
            assertThatThrownBy(param::validate)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("파일은 필수입니다.");
        }

        @Test
        @DisplayName("모든 파라미터가 유효하면 true를 반환한다")
        void param_withValidParams_returnsTrue() {
            // given
            UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(
                    1L, 100L, testFile
            );

            // when
            boolean result = param.validate();

            // then
            assertThat(result).isTrue();
        }
    }
}

