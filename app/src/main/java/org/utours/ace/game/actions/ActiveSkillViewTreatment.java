package org.utours.ace.game.actions;

import android.content.Context;
import android.view.View;

import org.utours.ace.common.Selectable;
import org.utours.ace.common.notify.Observable;
import org.utours.ace.game.dialog.ConfirmSkillDialog;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.GamePartyUpdater;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.function.Function;

public class ActiveSkillViewTreatment implements PlayerAction {

    private final Context context;
    private final GameService gameService;
    private final List<View> playerBoard;
    private final List<View> opponentBoard;
    private final Function<Card, ImageCard> imageRefs;
    private final GamePartyUpdater partyUpdater;
    private final Selectable<Integer> fromPos;
    private final Selectable<Integer> toPos;
    private final boolean isOpponent;
    private final AttackOpponentCardViewTreatment attackPlayerAction;

    public ActiveSkillViewTreatment(Context context,
                                    GameService gameService,
                                    List<View> playerBoard,
                                    List<View> opponentBoard,
                                    Function<Card, ImageCard> imageRefs,
                                    GamePartyUpdater partyUpdater,
                                    Selectable<Integer> fromPos,
                                    Selectable<Integer> toPos,
                                    boolean isToOpponent) {

        this.context = context;
        this.gameService = gameService;
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.imageRefs = imageRefs;
        this.partyUpdater = partyUpdater;
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.isOpponent = isToOpponent;
        this.attackPlayerAction = new AttackOpponentCardViewTreatment(
                playerBoard, opponentBoard, imageRefs,
                fromPos.getValue(), toPos.getValue());
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        View view = isOpponent
                ? opponentBoard.get(toPos.getValue())
                : playerBoard.get(toPos.getValue());

        Slot slotTo = isOpponent
                ? gameParty.opponentBoard().cards().get(toPos.getValue())
                : gameParty.playerBoard().cards().get(toPos.getValue());

        Slot slotFrom = gameParty.playerBoard().cards().get(fromPos.getValue());

        return process(gameParty, slotFrom, slotTo);
    }

    private GameParty process(GameParty gameParty, Slot slotFrom, Slot slotTo) {

        if (slotFrom instanceof Slot.SCard cardFrom && slotTo instanceof Slot.SCard cardTo) {

            var dialog = new ConfirmSkillDialog(context);

            dialog.setCardTo(cardTo.card(), imageRefs.apply(cardTo.card()));
            dialog.setCardFrom(cardFrom.card(), imageRefs.apply(cardFrom.card()));

            dialog.onActivateSkillButton(() -> {
                partyUpdater.update(gp -> gameService
                        .getSkillAction(cardFrom.card())
                        .apply(gameParty, cardFrom, cardTo)
                        .addPlayerAction(this));
                dialog.dismiss();
            });

            if (isOpponent) {
                dialog.onAttackButton(() -> {
                    partyUpdater.update(attackPlayerAction::apply);
                    dialog.dismiss();
                });
            } else {
                dialog.getAttackButton().setEnabled(false);
            }

            dialog.onCancelButton(dialog::dismiss);

            dialog.show();

            fromPos.unselect();
            toPos.unselect();
        }

        return gameParty;
    }

}
