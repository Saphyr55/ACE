package org.utours.ace.game.feature;


import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.PlayingTurn;
import org.utours.ace.profile.feature.CardService;

@DomainService
public class GameService {


    private final CardService cardService;

    public GameService(CardService cardService) {
        this.cardService = cardService;
    }

    public GameParty botTurn(PlayingTurn playingTurn, GameParty gameParty) {

        var botGameParty = GameParty.Builder.copyOf(gameParty)
                .withOpponent(gameParty.player())
                .withPlayer(gameParty.opponent())
                .build();

        var hand = botGameParty.player().hand();
        var cardSingleton = cardService.getCardsStochastic(hand, 1, false);
        var card = cardSingleton.stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        playingTurn.onTurn(botGameParty, card);

        // botGameParty = botGameParty.emplacePlayerCardInBoard(card);
        botGameParty = botGameParty.nextTurn();

        return GameParty.Builder.copyOf(botGameParty)
                .withPlayer(botGameParty.opponent())
                .withOpponent(botGameParty.player())
                .build();
    }



}
