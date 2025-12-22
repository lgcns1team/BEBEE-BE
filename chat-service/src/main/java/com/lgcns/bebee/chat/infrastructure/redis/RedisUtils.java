package com.lgcns.bebee.chat.infrastructure.redis;

public class RedisUtils {
    private static final String MEMBER_CHANNEL_PREFIX = "member:";

    private RedisUtils(){
    }

    /**
     * 회원 ID로 Redis 채널명을 생성합니다.
     * 1:1 채팅에서 각 회원은 자신의 채널을 구독하고,
     * 메시지는 수신자의 채널로 발행됩니다.
     *
     * @param memberId 회원 ID
     * @return Redis 채널명 (예: "member:123")
     */
    public static String getMemberChannel(Long memberId) {
        return MEMBER_CHANNEL_PREFIX + memberId;
    }
}
