package com.example.backend.check.model.factory;

import com.example.backend.check.model.CheckMetadata;
import com.example.backend.check.model.CheckResult;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.factory.CheckResultFactory.createNameErrorCheckResult;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckResultFactoryTest {

    @Test
    public void createNameErrorCheckResult_whenAllProvided_thenReturnsCheckResult() {
        CheckMetadata metadata = new CheckMetadata("check-name", "category");
        String error = "Some error";

        CheckResult checkResult = createNameErrorCheckResult(metadata, error);

        assertEquals(metadata.getName(), checkResult.getMetadata().getName());
        assertEquals(metadata.getCategory(), checkResult.getMetadata().getCategory());
        assertEquals(error, checkResult.getError());
    }

}