package org.utours.ace.game.impl;

import android.media.Image;
import android.view.View;

import org.utours.ace.R;
import org.utours.ace.game.OnPressedViewBoard;
import org.utours.ace.game.PlayerCardView;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.feature.PlayingTurn;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class PlayerOnPressedViewBoard implements OnPressedViewBoard {

    private final GameService gameService;
    private final Map<View, Card> playerCardViews;
    private final PlayingTurn playingTurn;
    private final Function<Card, ImageCard> imagesRefs;

    public PlayerOnPressedViewBoard(GameService gameService,
                                    Map<View, Card> playerCardViews,
                                    PlayingTurn playingTurn,
                                    Function<Card, ImageCard> imagesRefs) {

        this.gameService = gameService;
        this.playerCardViews = playerCardViews;
        this.playingTurn = playingTurn;
        this.imagesRefs = imagesRefs;
    }

    @Override
    public GameParty onPressed(GameParty gameParty, View selectedCardView, View view) {

        var emptyView = view.findViewById(R.id.view_emptyGameBoardCard);
        if (!checkCanPutCard(gameParty, selectedCardView, emptyView)) return gameParty;

        var card = playerCardViews.remove(selectedCardView);
        viewTreatmentOnPressedEmptyCardBoard(card, selectedCardView, view, emptyView);

        gameParty = gameParty.emplacePlayerCardInBoard(card);
        gameParty = gameParty.nextTurn();

        return gameParty;
    }

    private void viewTreatmentOnPressedEmptyCardBoard(Card card,
                                                      View selectedCardView,
                                                      View viewCard,
                                                      View emptyView) {

        var frameLayout = viewCard.findViewById(R.id.layout_gameBoardCard);
        var playerCardView = new PlayerCardView(frameLayout);

        selectedCardView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        playerCardView.bind(card, imagesRefs.apply(card));
    }

    private boolean checkCanPutCard(GameParty gameParty, View selectedCardView, View v) {
        return !playerCardViews.isEmpty()
                && selectedCardView != null
                && gameParty.isPlayerTurn()
                && v.getVisibility() == View.VISIBLE;
    }

}
