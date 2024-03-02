package org.utours.ace.infra.auth;

import android.content.Context;

import org.utours.ace.R;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.common.Validator;

import java.util.regex.Pattern;

public class AuthAccountValidatorProvider {

    public final static String EMAIL_PATTERN = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static Validator<AuthAccount> validatorOf(Context context, AuthAccount authAccount) {

        var msgEmailIsBlank     = context.getString(R.string.fill_the_email_field);
        var msgPasswordIsBlank  = context.getString(R.string.fill_the_password_field);
        var msgPasswordLength   = context.getString(R.string.password_must_have_more_than_6_character);
        var msgIsNotAnEmail     = context.getString(R.string.is_not_a_valid_email);

        return Validator.of(authAccount)
                .validate(a -> Pattern.matches(EMAIL_PATTERN, a.email()), msgIsNotAnEmail)
                .invalidate(a -> a.email().isBlank(), msgEmailIsBlank)
                .invalidate(a -> a.password().isBlank(), msgPasswordIsBlank)
                .invalidate(a -> a.password().length() < 6, msgPasswordLength);
    }

}
