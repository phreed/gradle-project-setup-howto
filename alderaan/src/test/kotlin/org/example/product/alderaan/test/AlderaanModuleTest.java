package org.example.product.alderaan.test;

import org.example.product.corellia.fixtures.CorelliaModuleFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlderaanModuleTest {

    @Test
    void testModule() {
        assertEquals(3, new CorelliaModuleFixture().threeModules().size());
    }
}
