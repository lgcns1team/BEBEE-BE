package com.lgcns.bebee.match.domain.entity.vo;

import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;

public enum EngagementType {
    DAY,
    TERM;

    public static EngagementType from(String type) {
        try{
            return EngagementType.valueOf(type);
        }catch(IllegalArgumentException e){
            throw MatchInvalidParamErrors.INVALID_ENGAGEMENT_TYPE.toException();
        }
    }
}
