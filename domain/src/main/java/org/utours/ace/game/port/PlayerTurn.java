package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;

import java.util.List;

public interface PlayerTurn {

    GameParty apply(GameParty gameParty, List<PlayerAction> playerActions);

}
