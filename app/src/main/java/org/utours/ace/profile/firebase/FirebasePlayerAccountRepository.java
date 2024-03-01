package org.utours.ace.profile.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.utours.ace.common.annotation.Adapter;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Adapter
public class FirebasePlayerAccountRepository implements PlayerAccountRepository {

    private final DatabaseReference reference;
    private final FirebasePlayerAccountMapper mapper;
    private final Map<String, PlayerAccount> caches;

    public FirebasePlayerAccountRepository(DatabaseReference reference,
                                           FirebasePlayerAccountMapper mapper) {
        this.reference = reference;
        this.mapper = mapper;
        this.caches = new HashMap<>();
    }

    @Override
    public CompletableFuture<Optional<PlayerAccount>> getByUID(String uid) {

        if (caches.containsKey(uid)) {
            var opt = Optional.ofNullable(caches.get(uid));
            return CompletableFuture.completedFuture(opt);
        }

        CompletableFuture<Optional<PlayerAccount>> future =
                new CompletableFuture<>();

        reference.child(uid).get()
                .addOnCanceledListener(() -> future.complete(Optional.empty()))
                .addOnSuccessListener(task -> {
                    var fbPlayerAccount = task.getValue(FirebasePlayerAccount.class);
                    var playerAccount = mapper.map(fbPlayerAccount);
                    caches.put(playerAccount.uid(), playerAccount);
                    future.complete(Optional.of(playerAccount));
                });

        return future;
    }

    @Override
    public CompletableFuture<List<PlayerAccount>> getByUsername(String username) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void store(PlayerAccount playerAccount) {
        String uid = playerAccount.uid();
        FirebasePlayerAccount firebasePlayerAccount = mapper.map(playerAccount);
        reference.child(uid).setValue(firebasePlayerAccount);
    }

}
