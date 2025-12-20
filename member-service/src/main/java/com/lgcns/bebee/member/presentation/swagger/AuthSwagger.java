package com.lgcns.bebee.member.presentation.swagger;

import com.lgcns.bebee.member.presentation.dto.MemberLoginReqDTO;
import com.lgcns.bebee.member.presentation.dto.MemberSignUpReqDTO;
import com.lgcns.bebee.member.presentation.dto.TokenResDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

/**
 * 인증 API Swagger 인터페이스
 * Controller가 이 인터페이스를 구현하여 API 문서화
 */
public interface AuthSwagger {

    /**
     * 회원가입
     * 
     * @param request 회원가입 요청 정보
     * @return 액세스 토큰 및 만료 시간 (리프레시 토큰은 쿠키로 전송)
     */
    ResponseEntity<TokenResDTO> signup(MemberSignUpReqDTO request);

    /**
     * 로그인
     * 
     * @param request 로그인 요청 정보 (이메일, 비밀번호)
     * @return 액세스 토큰 및 만료 시간 (리프레시 토큰은 쿠키로 전송)
     */
    ResponseEntity<TokenResDTO> login(MemberLoginReqDTO request);

    /**
     * 토큰 재발급
     * 
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다.
     * 리프레시 토큰은 쿠키에서 자동으로 추출됩니다.
     * 
     * @param servletRequest HTTP 요청 (쿠키에서 리프레시 토큰 추출)
     * @return 새로운 액세스 토큰 및 만료 시간 (리프레시 토큰은 쿠키로 전송)
     */
    ResponseEntity<TokenResDTO> reissue(HttpServletRequest servletRequest);

    /**
     * 로그아웃
     * 
     * 리프레시 토큰을 블랙리스트에 추가하여 재사용을 방지합니다.
     * 리프레시 토큰은 쿠키에서 자동으로 추출됩니다.
     * 
     * @param request HTTP 요청 (쿠키에서 리프레시 토큰 추출)
     * @param response HTTP 응답 (쿠키 만료 설정)
     * @return 204 No Content
     */
    ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response);
}

