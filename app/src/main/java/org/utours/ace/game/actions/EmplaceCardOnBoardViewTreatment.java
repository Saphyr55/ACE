package org.utours.ace.game.actions;

import android.view.View;

import org.utours.ace.R;
import org.utours.ace.game.CardViewBinding;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Player;
import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.function.Function;

public class EmplaceCardOnBoardViewTreatment implements PlayerAction {

    private final EmplaceCardInBoardPlayerAction playerAction;
    private final List<View> playerCardViews;
    private final List<View> slotViews;
    private final Function<Card, ImageCard> imagesRefs;
    private final int cardPosition;
    private final int slotPosition;
    private final Player player;
    private final GamePartyMapper refresh;

    public EmplaceCardOnBoardViewTreatment(Player player,
                                           GamePartyMapper refresh,
                                           List<View> playerCardViews,
                                           List<View> playerSlotViews,
                                           Function<Card, ImageCard> imagesRefs,
                                           int cardPosition,
                                           int slotPosition) {

        this.player = player;
        this.refresh = refresh;
        this.playerAction = new EmplaceCardInBoardPlayerAction(cardPosition, slotPosition);
        this.playerCardViews = playerCardViews;
        this.slotViews = playerSlotViews;
        this.imagesRefs = imagesRefs;
        this.cardPosition = cardPosition;
        this.slotPosition = slotPosition;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        var hand = gameParty.currentPlayer().hand();
        var card = hand.get(cardPosition);
        var cardView = playerCardViews.get(cardPosition);
        var slotView = slotViews.get(slotPosition);
        var emptyView = slotView.findViewById(R.id.view_emptySlot);

        if (!checkCanPutCard(gameParty, emptyView))
            return gameParty;

        viewTreatmentOnPressedEmptyCardBoard(card, cardView, slotView);

        gameParty = playerAction.apply(gameParty);

        gameParty = gameParty.addPlayerAction(playerAction, refresh);

        return gameParty;
    }

    private void viewTreatmentOnPressedEmptyCardBoard(Card card, View cardView, View view) {

        var emptyView = view.findViewById(R.id.view_emptySlot);
        var frameLayout = view.findViewById(R.id.view_cardSlot);
        var playerCardView = new CardViewBinding(frameLayout);

        playerCardView.bind(card, imagesRefs.apply(card));

        cardView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    private boolean checkCanPutCard(GameParty gameParty, View emptyView) {
        return !playerCardViews.isEmpty()
                && gameParty.isCurrentPlayer(player)
                && emptyView.getVisibility() == View.VISIBLE;
    }




}
