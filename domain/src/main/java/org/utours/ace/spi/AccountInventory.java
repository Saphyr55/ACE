package org.utours.ace.spi;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.model.Account;

import java.util.Optional;

@Port
public interface AccountInventory {

    Optional<Account> getByUsername(String username);

    void addAccount(Account account);

}
