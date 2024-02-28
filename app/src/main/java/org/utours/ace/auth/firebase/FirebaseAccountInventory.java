package org.utours.ace.auth.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import org.utours.ace.model.Account;
import org.utours.ace.port.AccountInventory;

import java.util.Optional;

public class FirebaseAccountInventory implements AccountInventory {

    private final FirebaseFirestore firestore;

    public FirebaseAccountInventory(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Optional<Account> getByEmail(String email) {
        return Optional.empty();
    }

}
