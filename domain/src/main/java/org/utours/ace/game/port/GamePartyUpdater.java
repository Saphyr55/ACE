package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;

import java.util.function.Function;

@FunctionalInterface
public interface GamePartyUpdater {

    void update(GamePartyMapper mapper);

}
