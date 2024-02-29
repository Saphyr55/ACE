package org.utours.ace.profile.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.PlayerAccount;

import java.util.List;
import java.util.Optional;

@Port
public interface PlayerAccountInventory {

    Optional<PlayerAccount> getByUID(String uid);

    List<PlayerAccount> getByUsername(String username);

    void store(PlayerAccount playerAccount);

}
