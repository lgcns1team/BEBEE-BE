package com.lgcns.bebee.chat.core.utils;

public final class StompDestinationUtils {
    public static final String ENDPOINT = "/ws/chats";
    public static final String PUBLISH_PREFIX = "/pub";
    public static final String SUBSCRIBE_PREFIX = "/sub";

    private StompDestinationUtils() {
    }

    /**
     * STOMP destination으로 변환합니다.
     *
     * @param channel 채널명 (예: "member:123")
     * @return STOMP destination (예: "/sub/member:123")
     */
    public static String toDestination(String channel) {
        return SUBSCRIBE_PREFIX + "/" + channel;
    }
}