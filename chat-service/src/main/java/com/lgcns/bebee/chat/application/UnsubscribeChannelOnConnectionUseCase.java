package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.application.client.ChannelSubscriber;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnsubscribeChannelOnConnectionUseCase implements UseCase<UnsubscribeChannelOnConnectionUseCase.Param, Void> {
    private final ChannelSubscriber channelSubscriber;

    @Override
    public Void execute(Param param) {
        channelSubscriber.unsubscribeFromMember(param.memberId);
        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long memberId;
    }
}
