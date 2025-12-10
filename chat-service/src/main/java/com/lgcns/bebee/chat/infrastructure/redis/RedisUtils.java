package com.lgcns.bebee.chat.infrastructure.redis;

public class RedisUtils {
    private static final String CHATROOM_CHANNEL_PREFIX = "chatroom:";

    private RedisUtils(){
    }

    /**
     * 채팅방 ID로 Redis 채널명을 생성합니다.
     *
     * @param chatroomId 채팅방 ID
     * @return Redis 채널명 (예: "chatroom:123")
     */
    public static String getChatroomChannel(Long chatroomId) {
        return CHATROOM_CHANNEL_PREFIX + chatroomId;
    }
}
