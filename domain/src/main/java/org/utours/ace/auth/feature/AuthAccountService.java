package org.utours.ace.auth.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.auth.port.AuthAccountConnection;
import org.utours.ace.auth.port.AuthAccountInventory;

@DomainService
public class AuthAccountService {

    private final AuthAccountInventory authAccountInventory;
    private final AuthAccountConnection authAccountConnection;


    public AuthAccountService(AuthAccountInventory authAccountInventory,
                              AuthAccountConnection authAccountConnection) {
        this.authAccountInventory = authAccountInventory;
        this.authAccountConnection = authAccountConnection;
    }

}
