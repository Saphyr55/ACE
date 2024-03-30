package org.utours.ace.game.actions;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.feature.CardService;

import java.util.ArrayList;

public class OnPullCardPlayerAction implements PlayerAction {


    private final CardService cardService;

    public OnPullCardPlayerAction(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public GameParty apply(GameParty oldGameParty) {

        var gameParty = oldGameParty;

        var hand = gameParty.currentPlayer().hand();
        if (hand.size() >= 5) {
            return gameParty;
        }

        var deckCard = gameParty.currentPlayer().deck().cards();
        var cardSingleton = cardService.getCardsStochastic(deckCard, 1,true);
        var card = cardSingleton.stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        var newHand = new ArrayList<>(hand);
        newHand.add(card);

        var newCurrentPlayer = Player.Builder.copyOf(gameParty.currentPlayer())
                .withHand(newHand)
                .build();

        gameParty = GameParty.Builder.copyOf(gameParty)
                .withCurrentPlayer(newCurrentPlayer)
                .build();

        gameParty = gameParty.nextTurn();

        return gameParty;
    }

}
