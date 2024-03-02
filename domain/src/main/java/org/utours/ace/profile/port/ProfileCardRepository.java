package org.utours.ace.profile.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ProfileCard;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Port
public interface ProfileCardRepository {

    CompletableFuture<ProfileCard> getByUID(String uid);

    CompletableFuture<List<ProfileCard>> getByProfile(PlayerAccount playerAccount);

}
