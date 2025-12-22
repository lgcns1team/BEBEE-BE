package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.member.application.usecase.ApproveDocumentUseCase;
import com.lgcns.bebee.member.application.usecase.RejectDocumentUseCase;
import com.lgcns.bebee.member.application.usecase.UploadDocumentUseCase;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import com.lgcns.bebee.member.presentation.dto.req.DocumentRejectReqDTO;
import com.lgcns.bebee.member.presentation.dto.res.DocumentUploadResDTO;
import com.lgcns.bebee.member.presentation.dto.res.DocumentVerificationResDTO;
import com.lgcns.bebee.member.presentation.swagger.DocumentSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 문서 검증 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController implements DocumentSwagger {

    private final UploadDocumentUseCase uploadDocumentUseCase;
    private final ApproveDocumentUseCase approveDocumentUseCase;
    private final RejectDocumentUseCase rejectDocumentUseCase;
    private final DocumentManagement documentManagement;

    /**
     * 문서 업로드
     * @param memberId 회원 ID
     * @param documentId 문서 유형 ID
     * @param file 업로드 파일
     * @return 업로드 결과
     */
    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResDTO> uploadDocument(
            @RequestParam Long memberId,
            @RequestParam Long documentId,
            @RequestPart MultipartFile file
    ) {
        UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(memberId, documentId, file);
        Long verificationId = uploadDocumentUseCase.execute(param);

        // 저장된 검증 정보 조회
        DocumentVerification verification = documentManagement.load(verificationId);

        DocumentUploadResDTO response = DocumentUploadResDTO.of(
                verification.getId(),
                verification.getFileUrl(),
                verification.getForgeryScore(),
                verification.getSystemFlag(),
                verification.getStatus().name()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 문서 검증 정보 조회
     * @param verificationId 검증 ID
     * @return 검증 정보
     */
    @GetMapping("/{verificationId}")
    public ResponseEntity<DocumentVerificationResDTO> getVerification(
            @PathVariable Long verificationId
    ) {
        DocumentVerification verification = documentManagement.load(verificationId);
        return ResponseEntity.ok(DocumentVerificationResDTO.from(verification));
    }

    /**
     * PENDING 상태 문서 목록 조회 (관리자용)
     * @return PENDING 문서 목록
     */
    @GetMapping("/pending")
    public ResponseEntity<List<DocumentVerificationResDTO>> getPendingList() {
        List<DocumentVerification> pendingList = documentManagement.loadPendingList();
        List<DocumentVerificationResDTO> response = pendingList.stream()
                .map(DocumentVerificationResDTO::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * 문서 승인 (관리자용)
     * @param verificationId 검증 ID
     * @return 성공 응답
     */
    @PostMapping("/{verificationId}/approve")
    public ResponseEntity<Void> approveDocument(
            @PathVariable Long verificationId
    ) {
        ApproveDocumentUseCase.Param param = new ApproveDocumentUseCase.Param(verificationId);
        approveDocumentUseCase.execute(param);
        return ResponseEntity.ok().build();
    }

    /**
     * 문서 거절 (관리자용)
     * @param verificationId 검증 ID
     * @param request 거절 요청 (사유)
     * @return 성공 응답
     */
    @PostMapping("/{verificationId}/reject")
    public ResponseEntity<Void> rejectDocument(
            @PathVariable Long verificationId,
            @RequestBody DocumentRejectReqDTO request
    ) {
        RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(
                verificationId,
                request.getReason()
        );
        rejectDocumentUseCase.execute(param);
        return ResponseEntity.ok().build();
    }
}
