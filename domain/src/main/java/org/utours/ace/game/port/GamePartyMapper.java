package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;

@FunctionalInterface
public interface GamePartyMapper {

    GameParty map(GameParty gameParty);

}
