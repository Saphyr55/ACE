package org.utours.ace.auth.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.auth.model.AuthAccount;

import java.util.function.Consumer;


@Port
public interface AuthAccountRegistration {

    void register(AuthAccount authAccount,
                  Consumer<AuthAccount> onSuccess,
                  Consumer<Exception> onFailure);


}
