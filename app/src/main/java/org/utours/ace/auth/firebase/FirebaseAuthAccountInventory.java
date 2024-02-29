package org.utours.ace.auth.firebase;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountInventory;

import java.util.Optional;

public class FirebaseAuthAccountInventory implements AuthAccountInventory {


    @Override
    public Optional<AuthAccount> getByEmail(String email) {
        return Optional.empty();
    }

}
