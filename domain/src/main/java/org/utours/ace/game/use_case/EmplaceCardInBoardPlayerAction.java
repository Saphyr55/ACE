package org.utours.ace.game.use_case;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
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
        List<Card> board = new ArrayList<>(gameParty.playerBoard().cards());

        if (board.get(slotPosition) != null) {

            Card card = hand.remove(cardPosition);
            Player newPlayer = new Player(player.playerID(), List.copyOf(hand));

            board.add(slotPosition, card);

            Board newPlayerBoard = new Board(List.copyOf(board));

            return GameParty.Builder.copyOf(gameParty)
                    .withPlayer(newPlayer)
                    .withPlayerBoard(newPlayerBoard)
                    .build();
        }

        return gameParty;
    }

}
