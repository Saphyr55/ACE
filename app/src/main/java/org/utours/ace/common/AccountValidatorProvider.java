package org.utours.ace.common;

import android.content.Context;

import org.utours.ace.R;
import org.utours.ace.model.Account;

import java.util.regex.Pattern;

public interface AccountValidatorProvider {

    String EMAIL_PATTERN = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    Context context();
    
    default Validator<Account> validatorOf(Account account) {

        var msgEmailIsBlank     = context().getString(R.string.fill_the_email_field);
        var msgPasswordIsBlank  = context().getString(R.string.fill_the_password_field);
        var msgPasswordLength   = context().getString(R.string.password_must_have_more_than_6_character);

        return Validator.of(account)
                .validate(  a -> Pattern.matches(EMAIL_PATTERN, a.email()))
                .invalidate(a -> a.email().isBlank(), msgEmailIsBlank)
                .invalidate(a -> a.password().isBlank(), msgPasswordIsBlank)
                .invalidate(a -> a.password().length() < 6, msgPasswordLength);
    }

}
