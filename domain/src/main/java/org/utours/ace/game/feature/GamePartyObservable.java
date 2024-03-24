package org.utours.ace.game.feature;

import org.utours.ace.common.notify.Observable;
import org.utours.ace.common.notify.Property;
import org.utours.ace.game.model.GameParty;

import java.util.ArrayList;
import java.util.List;

public class GamePartyObservable implements Observable<GameParty> {


    private final List<Property<GameParty>> gameParties;

    public GamePartyObservable(GameParty gameParty) {
       // this.gameParty = gameParty;
        this.gameParties = new ArrayList<>();
    }

    @Override
    public void addProperty(Property<GameParty> property) {
        gameParties.add(property);
    }

    @Override
    public void removeProperty(Property<GameParty> property) {
        gameParties.remove(property);
    }

    @Override
    public void notifyProperties() {
        // gameParties.forEach(gamePartyProperty -> gamePartyProperty.update());
    }

}
