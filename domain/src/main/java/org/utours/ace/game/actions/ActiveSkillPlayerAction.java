package org.utours.ace.game.actions;

import org.utours.ace.game.model.Board;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.game.port.SkillAction;

public class ActiveSkillPlayerAction implements PlayerAction {

    private final SkillAction skillAction;
    private final int slotFromPos;
    private final int slotToPos;
    private final boolean isOpponent;

    public ActiveSkillPlayerAction(SkillAction skillAction,
                                   int slotFromPos,
                                   int slotToPos,
                                   boolean isOpponent) {

        this.skillAction = skillAction;
        this.slotFromPos = slotFromPos;
        this.slotToPos = slotToPos;
        this.isOpponent = isOpponent;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        Board playerBoard = gameParty.currentPlayerBoard();
        Board opponentBoard = gameParty.opponentPlayerBoard();

        Slot slotFrom = playerBoard.slots().get(slotFromPos);
        Slot slotTo = playerBoard.slots().get(slotToPos);
        if (isOpponent)
            slotTo = opponentBoard.slots().get(slotToPos);

        return skillAction.apply(gameParty, slotFrom, slotTo);
    }

}
