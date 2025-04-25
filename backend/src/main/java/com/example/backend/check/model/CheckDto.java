package com.example.backend.check.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CheckDto {

    private final String name;

    public static CheckDto from(Check check) {
        if (check == null) {
            log.error("Check object is null");
        }
        return new CheckDto(check.getName());
    }

}
