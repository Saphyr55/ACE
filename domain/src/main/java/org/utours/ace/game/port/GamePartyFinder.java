package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.profile.model.PlayerAccount;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface GamePartyFinder {

    CompletableFuture<GameParty> join(PlayerAccount player, Consumer<GameParty> onUpdate);
}
