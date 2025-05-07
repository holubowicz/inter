package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckResult;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckResultFactory {

    public static CheckResult createNameErrorCheckResult(String name, String error) {
        return CheckResult.builder().name(name).error(error).build();
    }

}
