package com.lgcns.bebee.match.common.util;

import java.time.LocalDate;
import java.util.List;

public class ParamValidator {

    public static <T> boolean isNotNull(T value) {
        return value != null;
    }

    public static boolean isNonNegativeInteger(Integer value) {
        return value != null && value >= 0;
    }

    public static boolean isValidString(String str) {
        return str != null && !str.isBlank();
    }

    public static boolean isValidStringLength(String str, int minLength, int maxLength) {
        if (str == null || str.isBlank()) {
            return false;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }

    public static boolean isValidList(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
