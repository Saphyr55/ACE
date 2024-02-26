package org.utours.ace.api;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.model.Account;
import org.utours.ace.spi.AccountConnection;
import org.utours.ace.spi.AccountInventory;

@DomainService
public class AccountService {

    private final AccountInventory accountInventory;
    private final AccountConnection accountConnection;


    public AccountService(AccountInventory accountInventory,
                          AccountConnection accountConnection) {
        this.accountInventory = accountInventory;
        this.accountConnection = accountConnection;
    }


    public void connect(Account account) {
        accountConnection.connect(account);
    }

    public boolean isConnected(Account account) {
        return accountConnection.isConnected(account);
    }

}
