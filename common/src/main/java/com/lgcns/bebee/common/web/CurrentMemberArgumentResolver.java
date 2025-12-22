package com.lgcns.bebee.common.web;

import com.lgcns.bebee.common.annotation.CurrentMember;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.lgcns.bebee.common.web.AuthenticationUtil.*;

@Component
public class CurrentMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMember.class) && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Object memberKey = request.getAttribute(MEMBER_KEY);

        if(memberKey == null){
            throw new IllegalStateException("인증된 사용자 정보가 없습니다. @CurrentMember는 인증된 사용에서만 사용 가능합니다.");
        }
        return memberKey;
    }
}
