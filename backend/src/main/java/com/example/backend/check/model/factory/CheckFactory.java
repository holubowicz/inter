package com.example.backend.check.model.factory;

import com.example.backend.check.common.validator.ErrorValidator;
import com.example.backend.check.common.validator.NameValidator;
import com.example.backend.check.common.validator.QueryValidator;
import com.example.backend.check.model.Check;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CheckFactory {

    public static Check createCheck(String name, String query) {
        NameValidator.validate(name);
        QueryValidator.validate(query);
        return Check.builder().name(name).query(query).build();
    }

    public static Check createErrorCheck(String error) {
        ErrorValidator.validate(error);
        return Check.builder().error(error).build();
    }

    public static Check createNameErrorCheck(String name, String error) {
        NameValidator.validate(name);
        ErrorValidator.validate(error);
        return Check.builder().name(name).error(error).build();
    }

}
