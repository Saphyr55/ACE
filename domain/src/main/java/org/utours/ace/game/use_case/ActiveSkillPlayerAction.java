package org.utours.ace.game.use_case;

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
        Slot slotFrom = gameParty.playerBoard().cards().get(slotFromPos);
        Slot slotTo = gameParty.playerBoard().cards().get(slotToPos);
        return skillAction.process(gameParty, slotFrom, slotTo);
    }

}
