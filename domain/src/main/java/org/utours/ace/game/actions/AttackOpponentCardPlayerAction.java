package org.utours.ace.game.actions;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
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

        Board playerBoard = gameParty.currentPlayerBoard();
        Board opponentBoard = gameParty.opponentPlayerBoard();

        Slot playerSlot = playerBoard.slots().get(playerCardPos);
        Slot opponentSlot = opponentBoard.slots().get(opponentCardPos);

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

        playerBoard.slots().set(playerCardPos, newPlayerSlot);
        opponentBoard.slots().set(opponentCardPos, newOpponentSlot);

        return GameParty.Builder.copyOf(gameParty)
                .withCurrentPlayerBoard(playerBoard)
                .withOpponentBoard(opponentBoard)
                .build();
    }


}
