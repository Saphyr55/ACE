package org.utours.ace.game.feature;


import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.game.port.SkillAction;
import org.utours.ace.game.skills.PriestSkillAction;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.model.Card;

@DomainService
public class GameService {
    
    private final CardService cardService;

    public GameService(CardService cardService) {
        this.cardService = cardService;
    }

    public SkillAction getSkillAction(Card card) {
        return switch (card.classCoin().type()) {
            case EMPTY -> SkillAction.nothing();
            case PRIEST -> new PriestSkillAction();
            case PALADIN -> SkillAction.nothing();
            case SHAMAN -> SkillAction.nothing();
            case MAGE -> SkillAction.nothing();
            case WARRIOR -> SkillAction.nothing();
        };
    }

    public GameParty botAction(PlayerAction playerAction, GameParty gameParty) {
        /*
        var botGameParty = GameParty.Builder.copyOf(gameParty)
                .withOpponent(gameParty.firstPlayer())
                .withFirstPlayer(gameParty.secondPlayer())
                .build();

        var hand = botGameParty.currentPlayer().hand();
        var cardSingleton = cardService.getCardsStochastic(hand, 1, false);
        var card = cardSingleton.stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        playerAction.apply(botGameParty);

        // botGameParty = botGameParty.emplacePlayerCardInBoard(card);
        botGameParty = botGameParty.nextTurn();

        return GameParty.Builder.copyOf(botGameParty)
                .withFirstPlayer(botGameParty.secondPlayer())
                .withOpponent(botGameParty.firstPlayer())
                .build();
         */
        return gameParty;
    }



}
