package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.auth.port.AuthAccountConnection;
import org.utours.ace.auth.port.AuthAccountRegistration;
import org.utours.ace.auth.port.AuthenticationProvider;

public final class FirebaseAuthenticationProvider implements AuthenticationProvider {

    private final AuthAccountConnection authAccountConnection;
    private final AuthAccountRegistration authAccountRegistration;
    private final FirebaseAuthAccountMapper mapper;

    public FirebaseAuthenticationProvider(FirebaseAuth auth) {
        this.mapper = new FirebaseAuthAccountMapper();
        this.authAccountConnection = new FirebaseAuthAccountConnection(auth, mapper);
        this.authAccountRegistration = new FirebaseAuthAccountRegistration(auth, mapper);
    }

    @Override
    public AuthAccountConnection accountConnection() {
        return authAccountConnection;
    }

    @Override
    public AuthAccountRegistration accountRegistration() {
        return authAccountRegistration;
    }

}
