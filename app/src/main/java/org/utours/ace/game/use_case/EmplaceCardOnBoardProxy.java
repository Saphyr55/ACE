package org.utours.ace.game.use_case;

import android.view.View;

import org.utours.ace.R;
import org.utours.ace.game.PlayerCardView;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EmplaceCardOnBoardProxy implements PlayerAction {

    private final GameService gameService;
    private final Map<View, Card> playerCardViews;
    private final Function<Card, ImageCard> imagesRefs;
    private View view;
    private View selectedCardView;
    private EmplaceCardInBoardPlayerAction playerAction;


    public EmplaceCardOnBoardProxy(GameService gameService,
                                   Map<View, Card> playerCardViews,
                                   Function<Card, ImageCard> imagesRefs,
                                   int cardPosition,
                                   int slotPosition) {

        this.gameService = gameService;
        this.playerCardViews = playerCardViews;
        this.imagesRefs = imagesRefs;
        this.playerAction = new EmplaceCardInBoardPlayerAction(cardPosition, slotPosition);
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        var emptyView = view.findViewById(R.id.view_emptyGameBoardCard);

        if (!checkCanPutCard(gameParty, selectedCardView, emptyView))
            return gameParty;

        var card = playerCardViews.remove(selectedCardView);

        viewTreatmentOnPressedEmptyCardBoard(card, emptyView);

        return playerAction.apply(gameParty);
    }

    private void viewTreatmentOnPressedEmptyCardBoard(Card card, View emptyView) {

        var frameLayout = view.findViewById(R.id.view_gameBoardCard);
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
