package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountConnection;

import java.util.function.Consumer;

public final class FirebaseAuthAccountConnection implements AuthAccountConnection {

    private final FirebaseAuth auth;
    private final FirebaseAuthAccountMapper mapper;

    public FirebaseAuthAccountConnection(FirebaseAuth auth,
                                         FirebaseAuthAccountMapper mapper) {
        this.auth = auth;
        this.mapper = mapper;
    }

    @Override
    public void connect(AuthAccount authAccount,
                        Consumer<AuthAccount> onSuccess,
                        Consumer<Exception> onFailure) {

        auth.signInWithEmailAndPassword(authAccount.email(), authAccount.password())
                .addOnFailureListener(onFailure::accept)
                .addOnSuccessListener(result -> {
                    assert result.getUser() != null;
                    onSuccess.accept(mapper.map(result.getUser(), authAccount.password()));
                });
    }

}
