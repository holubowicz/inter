package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckResult;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.factory.CheckResultFactory.createNameErrorCheckResult;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckResultFactoryTest {

    @Test
    void createNameErrorCheckResult_whenAllProvided_thenReturnsCheckResult() {
        String name = "check-name";
        String error = "Some error";

        CheckResult checkResult = createNameErrorCheckResult(name, error);

        assertEquals(name, checkResult.getName());
        assertEquals(error, checkResult.getError());
    }

}