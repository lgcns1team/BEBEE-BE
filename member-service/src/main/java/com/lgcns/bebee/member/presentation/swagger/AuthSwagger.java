package com.lgcns.bebee.member.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.presentation.dto.req.MemberLoginReqDTO;
import com.lgcns.bebee.member.presentation.dto.req.MemberSignUpReqDTO;
import com.lgcns.bebee.member.presentation.dto.res.LoginResDTO;
import com.lgcns.bebee.member.presentation.dto.res.ReissueResDTO;
import com.lgcns.bebee.member.presentation.dto.res.SignUpResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 인증 API Swagger 인터페이스
 * Controller가 이 인터페이스를 구현하여 API 문서화
 */
@Tag(name = "인증", description = "회원 인증 관련 API")
public interface AuthSwagger {

    @Operation(
            summary = "회원가입",
            description = """
                    신규 회원을 등록합니다.

                    - 이메일, 비밀번호, 이름, 닉네임, 생년월일, 성별, 전화번호, 역할이 필수입니다.
                    - 주소(도로명), 위도, 경도, 행정구역 코드는 선택입니다.
                    - 이메일은 고유해야 하며 중복 시 409 에러가 발생합니다.
                    - 비밀번호는 암호화되어 저장됩니다.
                    - 회원 상태는 PENDING_APPROVAL로 설정됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 파라미터 누락 또는 유효성 검증 실패)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<SignUpResDTO> signup(
            @Parameter(
                    description = """
                            회원가입 요청 정보
                            - email: 이메일 (필수, 고유)
                            - password: 비밀번호 (필수)
                            - name: 이름 (필수)
                            - nickname: 닉네임 (필수)
                            - birthDate: 생년월일 (필수, yyyy-MM-dd)
                            - gender: 성별 (필수, MALE/FEMALE)
                            - phoneNumber: 전화번호 (필수)
                            - role: 역할 (필수, MEMBER/ADMIN)
                            - addressRoad: 도로명 주소 (선택)
                            - latitude: 위도 (선택)
                            - longitude: 경도 (선택)
                            - districtCode: 행정구역 코드 (선택)
                            """,
                    required = true
            )
            @RequestBody MemberSignUpReqDTO reqDTO
    );

    @Operation(
            summary = "로그인",
            description = """
                    이메일과 비밀번호로 로그인합니다.

                    - 이메일과 비밀번호를 검증합니다.
                    - 회원 상태가 ACTIVE 또는 PENDING_APPROVAL인 경우만 로그인 가능합니다.
                    - REJECTED(승인 거절) 또는 WITHDRAWN(탈퇴) 상태는 로그인 불가합니다.
                    - 로그인 성공 시 액세스 토큰을 응답 본문으로 반환합니다.
                    - 리프레시 토큰은 HttpOnly, Secure 쿠키로 전송됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 파라미터 누락)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "이메일 또는 비밀번호 불일치",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "계정 상태로 인한 로그인 불가 (탈퇴 또는 승인 거절)",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<LoginResDTO> login(
            @Parameter(
                    description = """
                            로그인 요청 정보
                            - email: 이메일 (필수)
                            - password: 비밀번호 (필수)
                            """,
                    required = true
            )
            @RequestBody MemberLoginReqDTO reqDTO
    );

    @Operation(
            summary = "토큰 재발급",
            description = """
                    리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다.

                    - 쿠키에서 리프레시 토큰을 자동으로 추출합니다.
                    - 리프레시 토큰이 유효하고 Redis에 저장된 값과 일치해야 합니다.
                    - 새로운 액세스 토큰을 응답 본문으로 반환합니다.
                    - 새로운 리프레시 토큰은 HttpOnly, Secure 쿠키로 전송됩니다.
                    - 기존 리프레시 토큰은 Redis에서 삭제되고 새 토큰으로 대체됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReissueResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "리프레시 토큰이 없거나 유효하지 않음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<ReissueResDTO> reissue(
            @Parameter(
                    description = "현재 인증된 회원 ID (액세스 토큰에서 추출)",
                    required = true,
                    example = "1"
            )
            @CurrentMember Long memberId,

            @Parameter(
                    description = "리프레시 토큰 (쿠키에서 자동 추출)",
                    required = false
            )
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    );

    @Operation(
            summary = "로그아웃",
            description = """
                    로그아웃 처리를 수행합니다.

                    - 액세스 토큰을 블랙리스트에 추가하여 해당 토큰의 재사용을 방지합니다.
                    - Redis에 저장된 리프레시 토큰을 삭제합니다.
                    - 리프레시 토큰 쿠키를 만료시킵니다.
                    - 액세스 토큰은 만료 시간까지만 블랙리스트에 유지됩니다 (TTL 설정).
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (액세스 토큰이 없거나 유효하지 않음)",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<Void> logout(
            @Parameter(hidden = true)
            HttpServletRequest request,

            @Parameter(hidden = true)
            HttpServletResponse response,

            @Parameter(
                    description = "현재 인증된 회원 ID (액세스 토큰에서 추출)",
                    required = true,
                    example = "1"
            )
            @CurrentMember Long memberId,

            @Parameter(
                    description = "리프레시 토큰 (쿠키에서 자동 추출)",
                    required = false
            )
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    );
}

