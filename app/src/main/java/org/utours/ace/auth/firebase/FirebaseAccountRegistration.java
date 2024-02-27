package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.model.Account;
import org.utours.ace.port.AccountRegistration;

import java.util.function.Consumer;

public class FirebaseAccountRegistration implements AccountRegistration {

    private final FirebaseAuth auth;

    public FirebaseAccountRegistration(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void register(Account account,
                         Consumer<Account> onSuccess,
                         Consumer<Exception> onFailure) {

        auth.createUserWithEmailAndPassword(account.email(), account.password())
                .addOnSuccessListener(result -> onSuccess.accept(account))
                .addOnFailureListener(onFailure::accept);
    }

}
