package org.utours.ace.infra.auth;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.infra.auth.firebase.FirebaseAuthAccountMapper;
import org.utours.ace.infra.auth.firebase.FirebaseAuthenticationProvider;
import org.utours.ace.infra.auth.firebase.FirebaseCurrentAuthAccountProvider;
import org.utours.ace.auth.port.AuthenticationProvider;
import org.utours.ace.auth.port.CurrentAuthAccountProvider;
import org.utours.ace.infra.sqlite.SQLiteACEDatabase;

import java.io.Closeable;

public enum AuthenticationContainer implements Closeable {
    INSTANCE;

    private final AuthenticationProvider authenticationProvider;
    private final CurrentAuthAccountProvider currentAuthAccountProvider;
    private final SQLiteACEDatabase sqLiteACEDatabase;


    AuthenticationContainer() {
        this.sqLiteACEDatabase = new SQLiteACEDatabase(null);

        var mapper = new FirebaseAuthAccountMapper();
        var auth = FirebaseAuth.getInstance();

        var firebaseAuthenticationProvider = new FirebaseAuthenticationProvider(auth, mapper);
        var firebaseCurrentAuthAccountProvider = new FirebaseCurrentAuthAccountProvider(auth, mapper);

        this.authenticationProvider = firebaseAuthenticationProvider;
        this.currentAuthAccountProvider = firebaseCurrentAuthAccountProvider;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public CurrentAuthAccountProvider getCurrentAuthAccountProvider() {
        return currentAuthAccountProvider;
    }

    @Override
    public synchronized void close() {
        sqLiteACEDatabase.close();
    }

}
