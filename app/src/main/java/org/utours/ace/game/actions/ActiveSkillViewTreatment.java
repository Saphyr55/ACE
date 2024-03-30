package org.utours.ace.game.actions;

import android.content.Context;
import android.view.View;

import org.utours.ace.common.Selectable;
import org.utours.ace.game.dialog.ConfirmSkillDialog;
import org.utours.ace.game.feature.GameService;
import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.game.port.GamePartyUpdater;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.profile.feature.ImageCardReference;

import java.util.List;

public class ActiveSkillViewTreatment implements PlayerAction {

    private final AttackOpponentCardViewTreatment attackPlayerAction;
    private final Context context;
    private final GamePartyMapper refresh;
    private final GameService gameService;
    private final ImageCardReference imageRefs;
    private final GamePartyUpdater partyUpdater;
    private final Selectable<Integer> fromPos;
    private final Selectable<Integer> toPos;
    private final boolean isOpponentSlotSelected;

    public ActiveSkillViewTreatment(Context context,
                                    GamePartyMapper refresh,
                                    GameService gameService,
                                    List<View> playerBoard,
                                    List<View> opponentBoard,
                                    ImageCardReference imageRefs,
                                    GamePartyUpdater partyUpdater,
                                    Selectable<Integer> fromPos,
                                    Selectable<Integer> toPos,
                                    boolean isToOpponent) {

        this.context = context;
        this.refresh = refresh;
        this.gameService = gameService;
        this.imageRefs = imageRefs;
        this.partyUpdater = partyUpdater;
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.isOpponentSlotSelected = isToOpponent;
        this.attackPlayerAction = new AttackOpponentCardViewTreatment(
                playerBoard, opponentBoard, imageRefs, refresh,
                fromPos.getValue(), toPos.getValue());
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        Board opponentBoard = gameParty.opponentPlayerBoard();
        Board playerBoard = gameParty.currentPlayerBoard();

        Slot slotFrom = playerBoard.slots().get(fromPos.getValue());

        Slot slotTo = isOpponentSlotSelected
                ? opponentBoard.slots().get(toPos.getValue())
                : playerBoard.slots().get(toPos.getValue());

        gameParty = process(gameParty, slotFrom, slotTo);

        return gameParty;
    }

    private GameParty process(GameParty gameParty, Slot slotFrom, Slot slotTo) {

        if (slotFrom instanceof Slot.SCard cardFrom && slotTo instanceof Slot.SCard cardTo) {

            var dialog = new ConfirmSkillDialog(context);

            dialog.setCardTo(cardTo.card(), imageRefs.get(cardTo.card()));
            dialog.setCardFrom(cardFrom.card(), imageRefs.get(cardFrom.card()));

            int posFrom = fromPos.getValue();
            int posTo = toPos.getValue();

            dialog.onActivateSkillButton(() -> {
                partyUpdater.update(gp -> activeSkill(gameParty, cardFrom, posFrom, posTo));
                fromPos.unselect();
                toPos.unselect();
                dialog.dismiss();
            });

            if (isOpponentSlotSelected) {
                dialog.onAttackButton(() -> {
                    partyUpdater.update(gp -> attackPlayerAction.apply(gameParty));
                    fromPos.unselect();
                    toPos.unselect();
                    dialog.dismiss();
                });
            } else {
                dialog.getAttackButton().setEnabled(false);
            }

            dialog.onCancelButton(() -> {
                fromPos.unselect();
                toPos.unselect();
                dialog.dismiss();
            });

            dialog.show();

        }

        return gameParty;
    }

    private GameParty activeSkill(GameParty gameParty, Slot.SCard cardFrom, int fromPos, int toPos) {

        PlayerAction activeSkillPlayerAction = new ActiveSkillPlayerAction(
                gameService.getSkillAction(cardFrom.card()),
                fromPos,
                toPos,
                isOpponentSlotSelected);

        gameParty = activeSkillPlayerAction
                .apply(gameParty)
                .addPlayerAction(activeSkillPlayerAction, refresh);

        return gameParty;
    }

}
