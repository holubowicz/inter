package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckMetadata;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckFactory {

    public static Check createCheck(CheckMetadata metadata, String query) {
        return Check.builder().metadata(metadata).query(query).build();
    }

    public static Check createErrorCheck(String error) {
        return Check.builder().error(error).build();
    }

    public static Check createNameErrorCheck(CheckMetadata metadata, String error) {
        return Check.builder().metadata(metadata).error(error).build();
    }

}
