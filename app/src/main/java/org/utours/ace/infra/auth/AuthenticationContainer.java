package org.utours.ace.infra.auth;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.auth.feature.AuthAccountService;
import org.utours.ace.infra.auth.firebase.FirebaseAuthAccountMapper;
import org.utours.ace.infra.auth.firebase.FirebaseAuthenticationProvider;
import org.utours.ace.infra.auth.firebase.FirebaseCurrentAuthAccountProvider;
import org.utours.ace.auth.port.AuthenticationProvider;
import org.utours.ace.auth.port.CurrentAuthAccountProvider;
import org.utours.ace.infra.sqlite.SQLiteACEDatabase;

import java.io.Closeable;

public enum AuthenticationContainer {
    INSTANCE;

    private final AuthenticationProvider authenticationProvider;
    private final CurrentAuthAccountProvider currentAuthAccountProvider;
    private final AuthAccountService authAccountService;

    AuthenticationContainer() {
        var mapper = new FirebaseAuthAccountMapper();
        var auth = FirebaseAuth.getInstance();

        var firebaseAuthenticationProvider = new FirebaseAuthenticationProvider(auth, mapper);
        var firebaseCurrentAuthAccountProvider = new FirebaseCurrentAuthAccountProvider(auth, mapper);

        this.authenticationProvider = firebaseAuthenticationProvider;
        this.currentAuthAccountProvider = firebaseCurrentAuthAccountProvider;
        this.authAccountService = new AuthAccountService(
             null,
                authenticationProvider,
                currentAuthAccountProvider
        );
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public CurrentAuthAccountProvider getCurrentAuthAccountProvider() {
        return currentAuthAccountProvider;
    }

    public AuthAccountService getService() {
        return authAccountService;
    }

}
