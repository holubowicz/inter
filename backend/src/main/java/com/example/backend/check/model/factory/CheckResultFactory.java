package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckMetadata;
import com.example.backend.check.model.CheckResult;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckResultFactory {

    public static CheckResult createNameErrorCheckResult(CheckMetadata metadata, String error) {
        return CheckResult.builder().metadata(metadata).error(error).build();
    }

}
