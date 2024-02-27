package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.model.Account;
import org.utours.ace.port.AccountConnection;

import java.util.function.Consumer;

public class FirebaseAccountConnection implements AccountConnection {

    private final FirebaseAuth auth;

    public FirebaseAccountConnection(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void connect(Account account,
                        Consumer<Account> onSuccess,
                        Consumer<Exception> onFailure) {

        auth.signInWithEmailAndPassword(account.email(), account.password())
                .addOnSuccessListener(result -> onSuccess.accept(account))
                .addOnFailureListener(onFailure::accept);
    }

}
