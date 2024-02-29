package org.utours.ace.profile.feature;

import org.utours.ace.profile.model.PlayerAccount;

import java.util.UUID;
import java.util.function.Function;

public interface PlayerAccountProvider {

    PlayerAccountProvider DEFAULT = (uid, email) ->
            new PlayerAccount(uid, "Player", email, 1, "");

    PlayerAccount provide(String uid, String email);


}
