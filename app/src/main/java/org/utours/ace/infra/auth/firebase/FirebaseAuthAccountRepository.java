package org.utours.ace.infra.auth.firebase;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountRepository;
import org.utours.ace.common.annotation.Adapter;

import java.util.Optional;

@Adapter
public class FirebaseAuthAccountRepository implements AuthAccountRepository {


    @Override
    public Optional<AuthAccount> getByEmail(String email) {
        return Optional.empty();
    }

}
