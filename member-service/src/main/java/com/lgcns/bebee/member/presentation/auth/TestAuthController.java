package com.lgcns.bebee.member.presentation.auth;

import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.infrastructure.security.CurrentMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 테스트용 컨트롤러
 * JWT 필터와 CurrentMember 어노테이션 동작 확인용
 */
@RestController
@RequestMapping("/api/test")
public class TestAuthController {

    /**
     * 현재 로그인한 회원 정보 조회 (인증 필요)
     * @param member 현재 로그인한 회원
     * @return 회원 정보
     */
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getCurrentMember(@CurrentMember Member member) {
        MemberInfoResponse response = new MemberInfoResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getRole().name()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 인증 없이 접근 가능한 엔드포인트
     * @return 공개 메시지
     */
    @GetMapping("/public")
    public ResponseEntity<PublicResponse> getPublic() {
        return ResponseEntity.ok(new PublicResponse("인증 없이 접근 가능합니다."));
    }

    private record MemberInfoResponse(
            Long memberId,
            String email,
            String name,
            String nickname,
            String role
    ) { }

    private record PublicResponse(String message) { }
}

