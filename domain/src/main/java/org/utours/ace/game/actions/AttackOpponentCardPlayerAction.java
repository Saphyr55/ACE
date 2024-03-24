package org.utours.ace.game.actions;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;

public class AttackOpponentCardPlayerAction implements PlayerAction  {

    private final int opponentCardPos;
    private final int playerCardPos;

    public AttackOpponentCardPlayerAction(int opponentCardPos,
                                          int playerCardPos) {

        this.opponentCardPos = opponentCardPos;
        this.playerCardPos = playerCardPos;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        Board playerBoard = gameParty.playerBoard();
        Board opponentBoard = gameParty.opponentBoard();

        Slot playerSlot = playerBoard.cards().get(playerCardPos);
        Slot opponentSlot = opponentBoard.cards().get(opponentCardPos);

        if (playerSlot instanceof Slot.SEmpty || opponentSlot instanceof Slot.SEmpty)
            return gameParty;

        Card playerCard = ((Slot.SCard) playerSlot).card();
        Card opponentCard = ((Slot.SCard) opponentSlot).card();

        int oldOpponentCardLifePoint = opponentCard.lifePoint();
        int newOpponentCardLifePoint = oldOpponentCardLifePoint - playerCard.attackPoint();

        int oldPlayerCardLifePoint = playerCard.lifePoint();
        int newPlayerCardLifePoint = oldPlayerCardLifePoint - opponentCard.attackPoint();

        playerCard = Card.Builder.copyOf(playerCard)
                .withLifePoint(newPlayerCardLifePoint)
                .build();

        opponentCard = Card.Builder.copyOf(playerCard)
                .withLifePoint(newOpponentCardLifePoint)
                .build();

        Slot newPlayerSlot = newPlayerCardLifePoint <= 0
                ? new Slot.SEmpty()
                : new Slot.SCard(playerCard);

        Slot newOpponentSlot = newOpponentCardLifePoint <= 0
                ? new Slot.SEmpty()
                : new Slot.SCard(opponentCard);

        gameParty.playerBoard().cards()
                .set(playerCardPos, newPlayerSlot);

        gameParty.opponentBoard().cards()
                .set(opponentCardPos, newOpponentSlot);

        return GameParty.Builder
                .copyOf(gameParty)
                .build();
    }


}
