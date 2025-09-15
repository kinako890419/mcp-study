package com.example.exercise.demo.enums;

import lombok.Getter;

@Getter
public enum ResponseStatusEnums {
    SUCCESS("success"),
    FAIL("fail"),
    UNAUTHORIZED("unauthorized"),
    UNAUTHENTICATED("unauthenticated");

    private final String status;

    ResponseStatusEnums(String status) {
        this.status = status;
    }

}
