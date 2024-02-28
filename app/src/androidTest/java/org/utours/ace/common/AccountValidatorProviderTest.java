package org.utours.ace.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.utours.ace.model.Account;

public class AccountValidatorProviderTest {

    AccountValidatorProvider validatorProvider =
            () -> InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void throw_when_empty_email() {

        var account = new Account(" ", "valid_password");
        var validator = validatorProvider.validatorOf(account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void throw_when_not_an_email() {

        var account = new Account("not_an_email", "valid_password");
        var validator = validatorProvider.validatorOf(account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void throw_when_empty_password() {

        var account = new Account("valid@email.com", "   ");
        var validator = validatorProvider.validatorOf(account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void when_illegal_password_length() {

        var account = new Account("valid@email.com", "12345");
        var validator = validatorProvider.validatorOf(account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }


}