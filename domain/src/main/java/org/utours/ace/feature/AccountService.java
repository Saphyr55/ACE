package org.utours.ace.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.port.AccountConnection;
import org.utours.ace.port.AccountProvider;

@DomainService
public final class AccountService {

    private final AccountProvider accountProvider;
    private final AccountConnection accountConnection;


    public AccountService(AccountProvider accountProvider,
                          AccountConnection accountConnection) {
        this.accountProvider = accountProvider;
        this.accountConnection = accountConnection;
    }

}
