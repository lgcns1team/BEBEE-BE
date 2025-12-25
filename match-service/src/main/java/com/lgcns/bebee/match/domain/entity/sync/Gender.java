package com.lgcns.bebee.match.domain.entity.sync;

import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;

public enum Gender {
    MALE, FEMALE, NONE
    ;

    public static Gender from(String value){
        try{
            return Gender.valueOf(value);
        }catch(IllegalArgumentException e){
            throw MatchInvalidParamErrors.INVALID_GENDER.toException();
        }
    }
}
