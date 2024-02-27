package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.port.AccountConnection;
import org.utours.ace.port.AccountRegistration;
import org.utours.ace.port.AuthenticationProvider;

public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    private AccountConnection accountConnection;
    private AccountRegistration accountRegistration;

    public FirebaseAuthenticationProvider(FirebaseAuth auth) {
        this.accountConnection = new FirebaseAccountConnection(auth);
        this.accountRegistration = new FirebaseAccountRegistration(auth);
    }

    @Override
    public AccountConnection accountConnection() {
        return accountConnection;
    }

    @Override
    public AccountRegistration accountRegistration() {
        return accountRegistration;
    }

}
