package org.utours.ace.game.actions;

import android.view.View;

import org.utours.ace.R;
import org.utours.ace.game.CardViewBinding;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.function.Function;

public class AttackOpponentCardViewTreatment implements PlayerAction {

    private final AttackOpponentCardPlayerAction playerAction;
    private final List<View> playerSlotViews;
    private final List<View> opponentSlotViews;
    private final Function<Card, ImageCard> imagesRefs;
    private final int opponentSlotPos;
    private final int playerSlotPos;

    public AttackOpponentCardViewTreatment(List<View> playerSlotViews,
                                           List<View> opponentSlotViews,
                                           Function<Card, ImageCard> imagesRefs,
                                           int cardFromPos,
                                           int cardToPos) {

        this.playerSlotViews = playerSlotViews;
        this.opponentSlotViews = opponentSlotViews;
        this.imagesRefs = imagesRefs;
        this.playerSlotPos = cardFromPos;
        this.opponentSlotPos = cardToPos;
        this.playerAction = new AttackOpponentCardPlayerAction(cardFromPos, cardToPos);
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        GameParty newGameParty = playerAction.apply(gameParty);

        View playerSlotView = playerSlotViews.get(playerSlotPos);
        View opponentSlotView = opponentSlotViews.get(opponentSlotPos);

        applyViewTreatment(newGameParty, playerSlotView, opponentSlotView);

        return newGameParty.addPlayerAction(this);
    }

    private void applyViewTreatment(GameParty gameParty, View playerSlotView, View opponentSlotView) {

        Slot opponentSlot = gameParty.opponentPlayerBoard().slots().get(opponentSlotPos);
        Slot playerSlot = gameParty.currentPlayerBoard().slots().get(playerSlotPos);

        View playerEmptyView = playerSlotView.findViewById(R.id.view_emptySlot);
        View playerFrameLayout = playerSlotView.findViewById(R.id.view_cardSlot);

        View opponentEmptyView = opponentSlotView.findViewById(R.id.view_emptySlot);
        View opponentFrameLayout = opponentSlotView.findViewById(R.id.view_cardSlot);

        if (playerSlot instanceof Slot.SEmpty) {
            playerEmptyView.setVisibility(View.VISIBLE);
            playerFrameLayout.setVisibility(View.GONE);
        }

        if (opponentSlot instanceof Slot.SEmpty) {
            opponentEmptyView.setVisibility(View.VISIBLE);
            opponentFrameLayout.setVisibility(View.GONE);
        }

        if (playerSlot instanceof Slot.SEmpty && opponentSlot instanceof Slot.SEmpty) {
            return;
        }

        if (playerSlot instanceof Slot.SCard playerSCard) {
            Card playerCard = playerSCard.card();

            CardViewBinding cardViewBinding = new CardViewBinding(playerFrameLayout);
            cardViewBinding.bind(playerCard, imagesRefs.apply(playerCard));
        }

        if (opponentSlot instanceof Slot.SCard opponentSCard) {
            Card card = opponentSCard.card();

            CardViewBinding cardViewBinding = new CardViewBinding(opponentFrameLayout);
            cardViewBinding.bind(card, imagesRefs.apply(card));
        }

    }

}
