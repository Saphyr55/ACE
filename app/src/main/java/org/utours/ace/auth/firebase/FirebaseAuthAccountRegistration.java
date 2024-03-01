package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.utours.ace.common.annotation.Adapter;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountRegistration;

import java.util.function.Consumer;

@Adapter
public final class FirebaseAuthAccountRegistration implements AuthAccountRegistration {

    private final FirebaseAuth auth;
    private final FirebaseAuthAccountMapper mapper;

    public FirebaseAuthAccountRegistration(FirebaseAuth auth,
                                           FirebaseAuthAccountMapper mapper) {
        this.auth = auth;
        this.mapper = mapper;
    }

    @Override
    public void register(AuthAccount authAccount,
                         Consumer<AuthAccount> onSuccess,
                         Consumer<Exception> onFailure) {

        auth.createUserWithEmailAndPassword(authAccount.email(), authAccount.password())
                .addOnFailureListener(onFailure::accept)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = result.getUser();
                    assert user != null;
                    AuthAccount account = mapper.map(user, authAccount.password());
                    onSuccess.accept(account);
                });
    }

}
