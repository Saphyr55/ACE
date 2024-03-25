package org.utours.ace.game.actions;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.PlayerAction;

public class EmptyPlayerAction implements PlayerAction {

    @Override
    public GameParty apply(GameParty gameParty) {
        return gameParty;
    }

}
