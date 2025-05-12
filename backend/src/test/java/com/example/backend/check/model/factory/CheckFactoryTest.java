package com.example.backend.check.model.factory;

import com.example.backend.check.model.Check;
import com.example.backend.check.model.CheckMetadata;
import org.junit.jupiter.api.Test;

import static com.example.backend.check.model.factory.CheckFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class CheckFactoryTest {

    @Test
    public void createCheck_whenCheckMetadataAndQueryProvided_thenReturnCheck() {
        CheckMetadata metadata = new CheckMetadata("check-name", "category");
        String query = "SELECT * FROM check";

        Check check = createCheck(metadata, query);

        assertNotNull(check);
        assertEquals(metadata.getName(), check.getMetadata().getName());
        assertEquals(metadata.getCategory(), check.getMetadata().getCategory());
        assertEquals(query, check.getQuery());
        assertNull(check.getError());
    }


    @Test
    public void createErrorCheck_whenErrorProvided_thenReturnsCheck() {
        String error = "some error";

        Check check = createErrorCheck(error);

        assertNotNull(check);
        assertEquals(error, check.getError());
        assertNull(check.getMetadata());
        assertNull(check.getQuery());
    }


    @Test
    public void createNameErrorCheck_whenAllProvided_thenReturnsCheck() {
        CheckMetadata metadata = new CheckMetadata("check-name", "category");
        String error = "some error";

        Check check = createNameErrorCheck(metadata, error);

        assertNotNull(check);
        assertEquals(metadata.getName(), check.getMetadata().getName());
        assertEquals(metadata.getCategory(), check.getMetadata().getCategory());
        assertEquals(error, check.getError());
        assertNull(check.getQuery());
    }

}