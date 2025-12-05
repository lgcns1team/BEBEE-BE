package com.lgcns.bebee.common.application;

public interface UseCase <P extends Params, R> {
    R execute(P params);
}
