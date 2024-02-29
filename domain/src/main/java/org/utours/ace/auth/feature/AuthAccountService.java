package org.utours.ace.auth.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.auth.port.AuthAccountConnection;
import org.utours.ace.auth.port.AuthAccountRepository;

@DomainService
public class AuthAccountService {

    private final AuthAccountRepository authAccountRepository;
    private final AuthAccountConnection authAccountConnection;


    public AuthAccountService(AuthAccountRepository authAccountRepository,
                              AuthAccountConnection authAccountConnection) {
        this.authAccountRepository = authAccountRepository;
        this.authAccountConnection = authAccountConnection;
    }

}
