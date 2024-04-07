package org.utours.ace.game.actions;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;

import java.util.ArrayList;
import java.util.List;

public class EmplaceCardInBoardPlayerAction implements PlayerAction {

    private final int cardPosition;
    private final int slotPosition;

    public EmplaceCardInBoardPlayerAction(int cardPosition,
                                          int slotPosition) {
        this.cardPosition = cardPosition;
        this.slotPosition = slotPosition;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        Player player = gameParty.currentPlayer();
        List<Card> hand = new ArrayList<>(player.hand());
        List<Slot> board = new ArrayList<>(gameParty.currentPlayerBoard().slots());

        if (board.get(slotPosition) instanceof Slot.SEmpty) {

            Card card = hand.set(cardPosition, null);
            Player newPlayer = Player.Builder.copyOf(player)
                    .withHand(hand)
                    .build();

            board.set(slotPosition, new Slot.SCard(card));

            Board newPlayerBoard = new Board(List.copyOf(board));

            return GameParty.Builder.copyOf(gameParty)
                    .withCurrentPlayer(newPlayer)
                    .withCurrentPlayerBoard(newPlayerBoard)
                    .build();
        }

        return gameParty;
    }

}
