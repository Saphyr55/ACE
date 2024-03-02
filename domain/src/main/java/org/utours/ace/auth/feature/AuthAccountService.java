package org.utours.ace.auth.feature;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountRegistration;
import org.utours.ace.auth.port.AuthenticationProvider;
import org.utours.ace.auth.port.CurrentAuthAccountProvider;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.auth.port.AuthAccountConnection;
import org.utours.ace.auth.port.AuthAccountRepository;

import java.util.Optional;
import java.util.function.Consumer;

@DomainService
public class AuthAccountService {

    private final AuthAccountRepository authAccountRepository;
    private final AuthenticationProvider authenticationProvider;
    private final CurrentAuthAccountProvider currentAuthAccountProvider;

    public AuthAccountService(AuthAccountRepository authAccountRepository,
                              AuthenticationProvider authenticationProvider,
                              CurrentAuthAccountProvider currentAuthAccountProvider) {

        this.authAccountRepository = authAccountRepository;
        this.authenticationProvider = authenticationProvider;
        this.currentAuthAccountProvider = currentAuthAccountProvider;
    }

    public void register(AuthAccount authAccount,
                         Consumer<AuthAccount> onSuccess,
                         Consumer<Exception> onFailure) {

        authenticationProvider
                .accountRegistration()
                .register(authAccount, onSuccess, onFailure);
    }

    public void connect(AuthAccount authAccount,
                        Consumer<AuthAccount> onSuccess,
                        Consumer<Exception> onFailure) {

        authenticationProvider
                .accountConnection()
                .connect(authAccount, onSuccess, onFailure);
    }

    public Optional<AuthAccount> currentAuthAccount() {
        return currentAuthAccountProvider.currentAuthAccount();
    }

}
