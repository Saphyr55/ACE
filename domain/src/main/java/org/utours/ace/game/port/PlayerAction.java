package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;

@FunctionalInterface
public interface PlayerAction {

    GameParty apply(GameParty gameParty);

    static PlayerAction noAction() {
        return gameParty -> gameParty;
    }

}
