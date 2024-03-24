package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;

@FunctionalInterface
public interface SkillAction {

    GameParty apply(GameParty gameParty, Slot slotFrom, Slot slotTo);

    static SkillAction nothing() {
        return (gameParty, slotFrom, slotTo) -> gameParty;
    }

}
