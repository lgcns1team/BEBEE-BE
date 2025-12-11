package com.lgcns.bebee.chat.application.client;

public interface ChannelSubscriber {
    void subscribeToMember(Long memberId);

    void unsubscribeFromMember(Long memberId);
}
