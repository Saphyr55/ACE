package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.CurrentAuthAccountProvider;
import org.utours.ace.common.annotation.Adapter;

import java.util.Optional;

@Adapter
public class FirebaseCurrentAuthAccountProvider implements CurrentAuthAccountProvider {


    private final FirebaseAuth auth;
    private final FirebaseAuthAccountMapper mapper;
    private AuthAccount cache;

    public FirebaseCurrentAuthAccountProvider(FirebaseAuth auth,
                                              FirebaseAuthAccountMapper mapper) {
        this.auth = auth;
        this.mapper = mapper;
        this.cache = null;
    }

    @Override
    public Optional<AuthAccount> currentAuthAccount() {
        if (cache == null) {
            FirebaseUser user = auth.getCurrentUser();
            cache = mapper.map(user);
        }
        return Optional.ofNullable(cache);
    }

}
