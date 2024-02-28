package org.utours.ace.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.model.Account;

import java.util.Optional;

@Port
public interface AccountProvider {

    Optional<Account> getByEmail(String email);

}
