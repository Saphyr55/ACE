package org.utours.ace.game.use_case;

import android.view.View;
import android.widget.GridLayout;

import org.utours.ace.game.OnPressViewBoard;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.port.PlayingTurn;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.Map;
import java.util.function.Function;

public class BotGamePlayingTurn implements PlayingTurn {

    private final GameService gameService;
    private final OnPressViewBoard onPressViewBoard;
    private final Map<View, Card> playerCardViews;
    private final GridLayout board;
    private int lastIndex;

    public BotGamePlayingTurn(GameService gameService,
                              Map<View, Card> playerCardViews,
                              Map<View, Card> opponentCardViews,
                              GridLayout board,
                              Function<Card, ImageCard> imageRefs) {

        this.lastIndex = 0;
        this.gameService = gameService;
        this.onPressViewBoard = new EmplaceCardOnBoardProxy(
                gameService,
                playerCardViews,
                opponentCardViews,
                imageRefs);
        this.playerCardViews = playerCardViews;
        this.board = board;
    }

    @Override
    public void onTurn(GameParty gameParty, Card card) {
        View selectedCardView = getSelectedCardView(card);
        View emptyViewToPlace = board.getChildAt(lastIndex++);
        onPressViewBoard.onPress(gameParty, selectedCardView, emptyViewToPlace);
    }

    private View getSelectedCardView(Card card) {
        View selectedCardView = null;
        for (var el : playerCardViews.entrySet()) {
            if (el.getValue().equals(card)) {
                selectedCardView = el.getKey();
                break;
            }
        }
        assert selectedCardView != null;
        return selectedCardView;
    }

}
