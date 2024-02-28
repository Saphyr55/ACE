package org.utours.ace.common;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;


public class ValidatorTest {


    @Test
    public void when_invalidate() {
        var validator = Validator.of(-9);

        validator.invalidate(integer -> integer < 0);

        assertFalse(validator.isValid());
        assertTrue(validator.isInvalid());

        validator.trace((integer, throwableStack) -> {
            assertEquals(-9, (long) integer);
            assertEquals(1, throwableStack.size());
        });

        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void when_validate() {
        var validator = Validator.of(-9);

        validator.validate(integer -> integer > 0);

        validator.trace((integer, throwableStack) -> {
            assertEquals(-9, (long) integer);
            assertEquals(1, throwableStack.size());
        });

        assertFalse(validator.isValid());
        assertTrue(validator.isInvalid());

        assertThrows(IllegalStateException.class, validator::get);
    }


}