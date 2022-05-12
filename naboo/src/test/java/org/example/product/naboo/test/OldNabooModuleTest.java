package org.example.product.naboo.test;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class OldNabooModuleTest {

    @Test
    public void testModuleOld4() {
        assertSame("Junit4", "Junit" + (2 + 2));
    }
}
