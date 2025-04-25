package com.example.backend.check;

import lombok.Builder;
import lombok.Getter;

// TODO: maybe, does something like CheckFactory?, using Factory principle

@Getter
@Builder
public class Check {

    private final String name;
    private final String query;
    private final String error;

}
