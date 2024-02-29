package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.auth.port.AuthAccountConnection;
import org.utours.ace.auth.port.AuthAccountRegistration;
import org.utours.ace.auth.port.AuthenticationProvider;
import org.utours.ace.common.annotation.Adapter;

@Adapter
public final class FirebaseAuthenticationProvider implements AuthenticationProvider {

    private final AuthAccountConnection authAccountConnection;
    private final AuthAccountRegistration authAccountRegistration;

    public FirebaseAuthenticationProvider(FirebaseAuth auth,
                                          FirebaseAuthAccountMapper mapper) {

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
