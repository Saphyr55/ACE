package org.utours.ace.game.feature;

import org.utours.ace.common.notify.Property;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.GamePartyUpdater;

public class GamePartyProperty implements Property<GameParty> {

    private final GamePartyUpdater gamePartyUpdater;

    public GamePartyProperty(GamePartyUpdater gamePartyUpdater) {
        this.gamePartyUpdater = gamePartyUpdater;
    }

    @Override
    public void update(GameParty value) {
        if (value.currentPlayer().actions().size() == 3) {
            gamePartyUpdater.update(GameParty::nextTurn);
        }
    }

}
