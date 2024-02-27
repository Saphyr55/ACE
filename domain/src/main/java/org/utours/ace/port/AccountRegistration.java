package org.utours.ace.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.model.Account;

import java.util.function.Consumer;

@Port
public interface AccountRegistration {

    void register(Account account,
                  Consumer<Account> onSuccess,
                  Consumer<Exception> onFailure);


}
