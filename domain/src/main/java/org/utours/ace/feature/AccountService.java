package org.utours.ace.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.model.Account;
import org.utours.ace.port.AccountConnection;
import org.utours.ace.port.AccountInventory;

@DomainService
public final class AccountService {

    private final AccountInventory accountInventory;
    private final AccountConnection accountConnection;


    public AccountService(AccountInventory accountInventory,
                          AccountConnection accountConnection) {
        this.accountInventory = accountInventory;
        this.accountConnection = accountConnection;
    }

}
