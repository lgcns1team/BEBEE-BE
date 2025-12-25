package com.lgcns.bebee.match.domain.entity.sync;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DisabilityCategory {
    PHYSICAL(1L, "지체장애"),
    VISUAL(2L, "시각장애"),
    HEARING(3L, "청각장애")
    ;

    private final Long id;
    private final String name;

    public static String getNameById(Long id) {
        return from(id).getName();
    }

    public static DisabilityCategory from(Long id) {
        return Arrays.stream(values())
                .filter(type -> type.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
    }
}
