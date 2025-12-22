package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.application.usecase.LoginUseCase;
import com.lgcns.bebee.member.application.usecase.LogoutUseCase;
import com.lgcns.bebee.member.application.usecase.ReissueTokenUseCase;
import com.lgcns.bebee.member.application.usecase.SignUpUseCase;
import com.lgcns.bebee.member.presentation.dto.req.MemberLoginReqDTO;
import com.lgcns.bebee.member.presentation.dto.req.MemberSignUpReqDTO;
import com.lgcns.bebee.member.presentation.dto.res.LoginResDTO;
import com.lgcns.bebee.member.presentation.dto.res.ReissueResDTO;
import com.lgcns.bebee.member.presentation.dto.res.SignUpResDTO;
import com.lgcns.bebee.member.presentation.swagger.AuthSwagger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lgcns.bebee.common.exception.AuthenticationErrors.*;
import static com.lgcns.bebee.common.web.AuthenticationUtil.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {
    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ReissueTokenUseCase reissueTokenUseCase;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResDTO> signup(@RequestBody MemberSignUpReqDTO reqDTO) {
        SignUpUseCase.Param param = new SignUpUseCase.Param(
                reqDTO.getEmail(),
                reqDTO.getPassword(),
                reqDTO.getName(),
                reqDTO.getNickname(),
                reqDTO.getBirthDate(),
                reqDTO.getGender(),
                reqDTO.getPhoneNumber(),
                reqDTO.getRole(),
                reqDTO.getAddressRoad(),
                reqDTO.getLatitude(),
                reqDTO.getLongitude(),
                reqDTO.getDistrictCode()
        );

        SignUpUseCase.Result result = signUpUseCase.execute(param);
        SignUpResDTO resDTO = SignUpResDTO.create(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(resDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody MemberLoginReqDTO reqDTO) {
        LoginUseCase.Param param = new LoginUseCase.Param(reqDTO.getEmail(), reqDTO.getPassword());

        LoginUseCase.Result result = loginUseCase.execute(param);
        LoginResDTO resDTO = new LoginResDTO(result.getAccessToken());

        ResponseCookie cookie = buildRefreshTokenCookie(result.getRefreshToken(), result.getRefreshTokenExpiresIn());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resDTO);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResDTO> reissue(
            @CurrentMember Long memberId,
            @CookieValue(name = "refresh_token", required = false) String refreshToken
            ) {
        if (refreshToken == null) {
            throw REFRESH_TOKEN_EXPIRED.toException();
        }

        ReissueTokenUseCase.Param param = new ReissueTokenUseCase.Param(memberId, refreshToken);

        ReissueTokenUseCase.Result result = reissueTokenUseCase.execute(param);
        ReissueResDTO resDTO = new ReissueResDTO(result.getAccessToken());

        ResponseCookie cookie = buildRefreshTokenCookie(result.getRefreshToken(), result.getRefreshTokenExpiresIn());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resDTO);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @CurrentMember Long memberId,
            @CookieValue(name = "refresh_token", required = false) String refreshToken
            ) {
        String accessToken = resolveToken(request);

        LogoutUseCase.Param param = new  LogoutUseCase.Param(
                memberId,
                accessToken,
                refreshToken
        );

        logoutUseCase.execute(param);

        ResponseCookie cookie = buildRefreshTokenCookie("", 0L);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    private ResponseCookie buildRefreshTokenCookie(String refreshToken, Long refreshTokenExpiresIn) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(refreshTokenExpiresIn)
                .build();
    }
}

