package org.utours.ace.game.use_case;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;

public class AttackOpponentCardPlayerAction implements PlayerAction  {

    private final int positionOpponentCard;
    private final int positionPlayerCard;

    public AttackOpponentCardPlayerAction(int positionOpponentCard,
                                          int positionPlayerCard) {
        this.positionOpponentCard = positionOpponentCard;
        this.positionPlayerCard = positionPlayerCard;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        Player player = gameParty.player();
        Player opponent = gameParty.opponent();

        Card playerCard = player.hand().get(positionPlayerCard);
        Card opponentCard = player.hand().get(positionOpponentCard);

        // Tu peux modifier l'etat des cartes grace Card.Builder
        playerCard = Card.Builder.copyOf(playerCard)
                .withLifePoint(0)
                .build();

        return GameParty.Builder.copyOf(gameParty)
                .build();
    }


}
