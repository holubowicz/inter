package com.example.backend.check.model;

import lombok.Data;

@Data
public class CheckDto {

    private final String name;

    public static final String CHECK_NULL_ERROR = "Check is null";

    public static CheckDto from(Check check) {
        if (check == null) {
            throw new IllegalArgumentException(CHECK_NULL_ERROR);
        }
        return new CheckDto(check.getName());
    }

}
