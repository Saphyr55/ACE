package org.utours.ace.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.utours.ace.infra.auth.AuthAccountValidatorProvider;
import org.utours.ace.auth.model.AuthAccount;

public class AuthAccountValidatorProviderTest {

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void throw_when_empty_email() {

        var account = new AuthAccount("", " ", "valid_password");
        var validator = AuthAccountValidatorProvider.validatorOf(context, account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void throw_when_not_an_email() {

        var account = new AuthAccount("","not_an_email", "valid_password");
        var validator = AuthAccountValidatorProvider.validatorOf(context, account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void throw_when_empty_password() {

        var account = new AuthAccount("","valid@email.com", "   ");
        var validator = AuthAccountValidatorProvider.validatorOf(context, account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }

    @Test
    public void when_illegal_password_length() {

        var account = new AuthAccount("","valid@email.com", "12345");
        var validator = AuthAccountValidatorProvider.validatorOf(context, account);

        assertTrue(validator.isInvalid());
        assertThrows(IllegalStateException.class, validator::get);
    }


}