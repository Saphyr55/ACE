package org.utours.ace.game.actions;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.PlayerAction;
import org.utours.ace.game.port.SkillAction;

public class ActiveSkillPlayerAction implements PlayerAction {

    private SkillAction skillAction;
    private final int slotFromPos;
    private final int slotToPos;

    public ActiveSkillPlayerAction(SkillAction skillAction, int slotFromPos, int slotToPos) {
        this.slotFromPos = slotFromPos;
        this.slotToPos = slotToPos;
    }

    @Override
    public GameParty apply(GameParty gameParty) {
        Slot slotFrom = gameParty.playerBoard().slots().get(slotFromPos);
        Slot slotTo = gameParty.playerBoard().slots().get(slotToPos);
        return skillAction.apply(gameParty, slotFrom, slotTo);
    }

}
