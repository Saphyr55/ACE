package org.utours.ace.auth;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.auth.firebase.FirebaseAuthAccountMapper;
import org.utours.ace.auth.firebase.FirebaseAuthenticationProvider;
import org.utours.ace.auth.firebase.FirebaseCurrentAuthAccountProvider;
import org.utours.ace.auth.port.AuthenticationProvider;
import org.utours.ace.auth.port.CurrentAuthAccountProvider;

public enum AuthenticationContainer {
    INSTANCE;

    private final AuthenticationProvider authenticationProvider;
    private final CurrentAuthAccountProvider currentAuthAccountProvider;

    AuthenticationContainer() {
        var mapper = new FirebaseAuthAccountMapper();
        var auth = FirebaseAuth.getInstance();
        this.authenticationProvider = new FirebaseAuthenticationProvider(auth, mapper);
        this.currentAuthAccountProvider = new FirebaseCurrentAuthAccountProvider(auth, mapper);
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public CurrentAuthAccountProvider getCurrentAuthAccountProvider() {
        return currentAuthAccountProvider;
    }

}
