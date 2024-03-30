package org.utours.ace.game.actions;

import android.view.View;

import org.utours.ace.R;
import org.utours.ace.game.CardViewBinding;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.feature.ImageCardReference;
import org.utours.ace.profile.model.Card;

import java.util.List;

public class AttackOpponentCardViewTreatment implements PlayerAction {

    private final AttackOpponentCardPlayerAction playerAction;
    private final List<View> playerSlotViews;
    private final List<View> opponentSlotViews;
    private final ImageCardReference imagesRefs;
    private final int opponentSlotPos;
    private final GamePartyMapper refresh;
    private final int playerSlotPos;

    public AttackOpponentCardViewTreatment(List<View> playerSlotViews,
                                           List<View> opponentSlotViews,
                                           ImageCardReference imagesRefs,
                                           GamePartyMapper refresh,
                                           int cardFromPos,
                                           int cardToPos) {

        this.playerSlotViews = playerSlotViews;
        this.opponentSlotViews = opponentSlotViews;
        this.imagesRefs = imagesRefs;
        this.refresh = refresh;
        this.playerSlotPos = cardFromPos;
        this.opponentSlotPos = cardToPos;
        this.playerAction = new AttackOpponentCardPlayerAction(cardFromPos, cardToPos);
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        View playerSlotView = playerSlotViews.get(playerSlotPos);
        View opponentSlotView = opponentSlotViews.get(opponentSlotPos);

        gameParty = playerAction.apply(gameParty);

        applyViewTreatment(gameParty, playerSlotView, opponentSlotView);

        return gameParty.addPlayerAction(playerAction, refresh);
    }

    private void applyViewTreatment(GameParty gameParty, View playerSlotView, View opponentSlotView) {

        Slot playerSlot = gameParty.currentPlayerBoard().slots().get(playerSlotPos);
        Slot opponentSlot = gameParty.opponentPlayerBoard().slots().get(opponentSlotPos);

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
            cardViewBinding.bind(playerCard, imagesRefs.get(playerCard));
        }

        if (opponentSlot instanceof Slot.SCard opponentSCard) {
            Card card = opponentSCard.card();
            CardViewBinding cardViewBinding = new CardViewBinding(opponentFrameLayout);
            cardViewBinding.bind(card, imagesRefs.get(card));
        }

    }

}
