package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import com.lgcns.bebee.member.domain.service.DocumentVerificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 문서 업로드 유스케이스
 * 파일 저장 → 위변조 분석 → 검증 결과 저장
 */
@Service
@RequiredArgsConstructor
public class UploadDocumentUseCase implements UseCase<UploadDocumentUseCase.Param, Long> {

    private final FileStorageClient fileStorageClient;
    private final DocumentVerificationService verificationService;
    private final DocumentManagement documentManagement;
    private final DocumentVerificationRepository verificationRepository;

    /**
     * 문서 업로드 실행
     * @param param 업로드 파라미터
     * @return 생성된 검증 ID
     */
    @Override
    @Transactional
    public Long execute(Param param) {
        // 파라미터 검증
        param.validate();

        // 1. 파일 저장 (Infrastructure)
        String fileUrl = fileStorageClient.upload(param.getFile(), "documents");

        // 2. 위변조 분석 (Domain Service)
        DocumentVerificationService.AnalysisResult analysis = verificationService.analyze(param.getFile());

        // 3. Document 조회 (Domain Service)
        Document document = documentManagement.loadDocument(param.getDocumentId());

        // 4. DocumentVerification 생성 및 분석 결과 적용
        DocumentVerification verification = DocumentVerification.of(fileUrl, document);
        verification.applyAnalysisResult(
                analysis.exifScore(),
                analysis.ocrScore(),
                analysis.forgeryScore(),
                analysis.systemFlag()
        );

        // 5. 저장
        verificationRepository.save(verification);

        return verification.getDocumentVerificationId();
    }

    /**
     * 업로드 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long documentId;
        private final MultipartFile file;

        @Override
        public boolean validate() {
            if (memberId == null) {
                throw new IllegalArgumentException("회원 ID는 필수입니다.");
            }
            if (documentId == null) {
                throw new IllegalArgumentException("문서 ID는 필수입니다.");
            }
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("파일은 필수입니다.");
            }
            return true;
        }
    }
}
