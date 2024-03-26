package org.utours.ace.game.skill;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;
import org.utours.ace.game.port.SkillAction;

public class PriestSkillAction implements SkillAction {


    @Override
    public GameParty apply(GameParty gameParty, Slot slotFrom, Slot slotTo) {

        return gameParty;
    }

}
