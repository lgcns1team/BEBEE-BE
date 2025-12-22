package com.lgcns.bebee.member.domain.entity.vo;

import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;

public enum Gender {
    MALE,
    FEMALE,
    NONE;

    public static Gender from(String value){
        try{
            return Gender.from(value.toUpperCase());
        }catch(IllegalArgumentException e){
            throw MemberInvalidParamErrors.INVALID_GENDER.toException();
        }
    }
}




