package org.utours.ace.auth;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.auth.firebase.FirebaseAuthenticationProvider;
import org.utours.ace.port.AuthenticationProvider;

public enum AuthenticationManager {
    INSTANCE;

    private final AuthenticationProvider authenticationProvider;

    AuthenticationManager() {
        this.authenticationProvider =
                new FirebaseAuthenticationProvider(FirebaseAuth.getInstance());
    }


    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }


}
