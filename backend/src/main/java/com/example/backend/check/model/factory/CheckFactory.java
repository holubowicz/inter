package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckFactory {

    public static Check createCheck(String name, String query) {
        return Check.builder().name(name).query(query).build();
    }

    public static Check createErrorCheck(String error) {
        return Check.builder().error(error).build();
    }

    public static Check createNameErrorCheck(String name, String error) {
        return Check.builder().name(name).error(error).build();
    }

}
