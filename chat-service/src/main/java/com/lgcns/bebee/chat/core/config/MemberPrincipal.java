package com.lgcns.bebee.chat.core.config;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class MemberPrincipal implements Principal {
    private final Long memberId;

    @Override
    public String getName() {
        return String.valueOf(memberId);
    }
}