package com.example.backend.check;

import lombok.Data;

@Data
public class CheckDto {

    private final String name;

    public static CheckDto from(Check check) {
        // TODO: remove?
        if (check == null) {
            throw new NullPointerException("The Check is null");
        }

        return new CheckDto(check.getName());
    }

}
