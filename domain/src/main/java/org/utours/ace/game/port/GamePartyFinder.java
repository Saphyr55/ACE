package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GamePartyFinder {

    CompletableFuture<Optional<GameParty>> find(Player player);

    CompletableFuture<GameParty> join(Player player);

}
