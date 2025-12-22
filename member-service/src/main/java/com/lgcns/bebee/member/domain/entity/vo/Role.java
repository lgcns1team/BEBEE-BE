package com.lgcns.bebee.member.domain.entity.vo;

import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;

public enum Role {
    ADMIN,
    USER,
    DISABLED,
    HELPER;

    public static Role from(String value){
        try{
            return Role.valueOf(value.toUpperCase());
        }catch(IllegalArgumentException e){
            throw MemberInvalidParamErrors.INVALID_ROLE.toException();
        }
    }
}

