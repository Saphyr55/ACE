package org.utours.ace.game.feature;


import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.model.Card;

import java.util.List;

@DomainService
public class GameService {


    private final CardService cardService;

    public GameService(CardService cardService) {
        this.cardService = cardService;
    }

    public GameParty botTurn(PlayingTurn playingTurn, GameParty gameParty) {

        GameParty botGameParty = GameParty.Builder.of(gameParty)
                .withOpponent(gameParty.player())
                .withPlayer(gameParty.opponent())
                .build();

        List<Card> hand = botGameParty.player().hand();
        List<Card> cardSingleton = cardService.getCardsStochastic(hand, 1, false);
        Card card = cardSingleton.stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        playingTurn.onTurn(botGameParty, card);

        botGameParty = botGameParty.emplacePlayerCardInBoard(card);
        botGameParty = botGameParty.nextTurn();

        return GameParty.Builder.of(botGameParty)
                .withPlayer(botGameParty.opponent())
                .withOpponent(botGameParty.player())
                .build();
    }

}
