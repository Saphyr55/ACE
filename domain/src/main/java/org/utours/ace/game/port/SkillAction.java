package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.model.Slot;

@FunctionalInterface
public interface SkillAction {

    GameParty apply(GameParty gameParty,
                    int slotFrom,
                    int slotTo,
                    boolean isOpponent);

    static SkillAction nothing() {
        return (gameParty, slotFrom, slotTo, isOpponent) -> gameParty;
    }

}
