package com.lgcns.bebee.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.common.config.JwtProperties;
import com.lgcns.bebee.member.presentation.dto.MemberLoginReqDTO;
import com.lgcns.bebee.member.presentation.dto.MemberSignUpReqDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 완전한 멤버 서비스 통합 테스트
 * 회원가입 → 문서등록 → 로그인 → 로그아웃 플로우
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("멤버 서비스 완전 통합 테스트")
class MemberServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @DisplayName("회원가입 → 로그인 완전 플로우")
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void completeMemberFlow() throws Exception {
        // 1. 회원가입
        MemberSignUpReqDTO signUpRequest = new MemberSignUpReqDTO();
        // Reflection을 사용해서 private 필드 설정
        setField(signUpRequest, "email", "test@example.com");
        setField(signUpRequest, "password", "Password123!");
        setField(signUpRequest, "name", "테스트유저");
        setField(signUpRequest, "nickname", "testuser");
        setField(signUpRequest, "birthDate", LocalDate.of(1990, 1, 1));
        setField(signUpRequest, "gender", "MALE");
        setField(signUpRequest, "phoneNumber", "010-1234-5678");
        setField(signUpRequest, "role", "USER");
        setField(signUpRequest, "addressRoad", "서울시 강남구 테헤란로 123");
        setField(signUpRequest, "latitude", 37.123456);
        setField(signUpRequest, "longitude", 127.123456);
        setField(signUpRequest, "districtCode", "12345");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(result -> {
                    System.out.println("Response Status: " + result.getResponse().getStatus());
                    System.out.println("Response Body: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists(jwtProperties.getRefreshCookieName()));

        // 2. 로그인
        MemberLoginReqDTO loginRequest = new MemberLoginReqDTO();
        setField(loginRequest, "email", "test@example.com");
        setField(loginRequest, "password", "Password123!");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists(jwtProperties.getRefreshCookieName()));

        // TODO: 3. 문서등록 (OCR 서비스 연동)
        // TODO: 4. 로그아웃
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
