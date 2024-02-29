package org.utours.ace.profile.firebase;

import com.google.firebase.database.DatabaseReference;

import org.utours.ace.common.annotation.Adapter;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountInventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Adapter
public class FirebasePlayerAccountRepository implements PlayerAccountInventory {

    private final DatabaseReference reference;
    private final FirebasePlayerAccountMapper mapper;

    public FirebasePlayerAccountRepository(DatabaseReference reference,
                                           FirebasePlayerAccountMapper mapper) {
        this.reference = reference;
        this.mapper = mapper;
    }

    @Override
    public Optional<PlayerAccount> getByUID(String uid) {

        FirebasePlayerAccount fbPlayerAccount =  reference
                .child(uid).get()
                .getResult()
                .getValue(FirebasePlayerAccount.class);

        PlayerAccount playerAccount = mapper.map(fbPlayerAccount);
        return Optional.ofNullable(playerAccount);
    }

    @Override
    public List<PlayerAccount> getByUsername(String username) {
        return List.of();
    }

    @Override
    public void store(PlayerAccount playerAccount) {
        String uid = playerAccount.uid();
        FirebasePlayerAccount firebasePlayerAccount = mapper.map(playerAccount);
        reference.child(uid).setValue(firebasePlayerAccount);
    }

}
