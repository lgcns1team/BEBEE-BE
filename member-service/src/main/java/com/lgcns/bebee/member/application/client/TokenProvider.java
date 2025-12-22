package com.lgcns.bebee.member.application.client;

import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.TokenInfo;

public interface TokenProvider {
    TokenInfo generateTokens(Member member);
}
