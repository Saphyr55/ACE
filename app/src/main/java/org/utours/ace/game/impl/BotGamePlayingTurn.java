package org.utours.ace.game.impl;

import android.view.View;
import android.widget.GridLayout;

import org.utours.ace.game.OnPressedViewBoard;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.feature.PlayingTurn;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class BotGamePlayingTurn implements PlayingTurn {

    private final GameService gameService;
    private final OnPressedViewBoard onPressedViewBoard;
    private final Map<View, Card> cardViews;
    private final GridLayout board;
    private int lastIndex;

    public BotGamePlayingTurn(GameService gameService,
                              Map<View, Card> cardViews,
                              GridLayout board,
                              Function<Card, ImageCard> imageRefs) {
        this.lastIndex = 0;
        this.gameService = gameService;
        this.onPressedViewBoard = new PlayerOnPressedViewBoard(gameService, cardViews, this, imageRefs);
        this.cardViews = cardViews;
        this.board = board;
    }

    @Override
    public void onTurn(GameParty gameParty, Card card) {
        // if (!cardViews.containsValue(card)) return;
        View selectedCardView = getSelectedCardView(card);
        View emptyViewToPlace = board.getChildAt(lastIndex++);
        onPressedViewBoard.onPressed(gameParty, selectedCardView, emptyViewToPlace);
    }

    private View getSelectedCardView(Card card) {
        View selectedCardView = null;
        for (var el : cardViews.entrySet()) {
            if (el.getValue().equals(card)) {
                selectedCardView = el.getKey();
                break;
            }
        }
        assert selectedCardView != null;
        return selectedCardView;
    }

}
