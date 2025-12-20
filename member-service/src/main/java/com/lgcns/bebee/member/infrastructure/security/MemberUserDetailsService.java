package com.lgcns.bebee.member.infrastructure.security;

import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Member 엔티티를 UserDetails로 변환하는 서비스
 * JwtAuthenticationFilter에서 사용됩니다.
 */
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + email));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole().name());
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(Collections.singletonList(authority))
                .build();
    }
}

