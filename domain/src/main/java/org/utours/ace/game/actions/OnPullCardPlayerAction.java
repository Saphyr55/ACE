package org.utours.ace.game.actions;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.feature.CardService;

import java.util.ArrayList;

public class OnPullCardPlayerAction implements PlayerAction {


    private final CardService cardService;
    private final GamePartyMapper gamePartyMapper;

    public OnPullCardPlayerAction(CardService cardService,
                                  GamePartyMapper gamePartyMapper) {
        this.cardService = cardService;
        this.gamePartyMapper = gamePartyMapper;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        var hand = gameParty.currentPlayer().hand();
        if (!hand.contains(null)) {
            return gameParty;
        }

        var deckCard = gameParty.currentPlayer().deck().cards();
        var cardSingleton = cardService.getCardsStochastic(deckCard, 1,false);
        var card = cardSingleton.stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        var newHand = new ArrayList<>(hand);
        var index = newHand.indexOf(null);
        newHand.set(index, card);

        var newCurrentPlayer = Player.Builder.copyOf(gameParty.currentPlayer())
                .withHand(newHand)
                .build();

        gameParty = GameParty.Builder.copyOf(gameParty)
                .withCurrentPlayer(newCurrentPlayer)
                .build();

        gameParty = gamePartyMapper.map(gameParty);
        return gameParty.addPlayerAction(this, gamePartyMapper);
    }

}
