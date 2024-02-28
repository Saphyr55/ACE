package org.utours.ace.auth.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import org.utours.ace.model.Account;
import org.utours.ace.port.AccountProvider;

import java.util.Optional;

public class FirebaseAccountProvider implements AccountProvider {

    private final FirebaseFirestore firestore;

    public FirebaseAccountProvider(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Optional<Account> getByEmail(String email) {
        return Optional.empty();
    }

}
