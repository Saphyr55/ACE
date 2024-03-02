package org.utours.ace.profile.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ProfileCard;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Port
public interface PlayerAccountRepository {

    CompletableFuture<Optional<PlayerAccount>> getByUID(String uid);

    CompletableFuture<List<PlayerAccount>> getByUsername(String username);

    void store(PlayerAccount playerAccount);

}
