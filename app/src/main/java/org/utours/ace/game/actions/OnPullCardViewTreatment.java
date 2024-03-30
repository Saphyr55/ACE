package org.utours.ace.game.actions;


import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.feature.CardService;

public class OnPullCardViewTreatment implements PlayerAction {

    private final PlayerAction playerAction;

    public OnPullCardViewTreatment(GamePartyMapper gamePartyMapper,
                                   CardService cardService) {

        this.playerAction = new OnPullCardPlayerAction(cardService, gamePartyMapper);
    }

    @Override
    public GameParty apply(GameParty gameParty) {
        gameParty = playerAction.apply(gameParty);
        return gameParty;
    }

}
