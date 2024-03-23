package org.utours.ace.game.use_case;

import android.app.Dialog;
import android.content.Context;

import org.utours.ace.game.dialog.ConfirmSkillDialog;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.PlayerAction;

public class ActiveSkillViewTreatment implements PlayerAction {

    private final Context context;

    public ActiveSkillViewTreatment(Context context) {
        this.context = context;
    }

    @Override
    public GameParty apply(GameParty gameParty) {

        var dialog = new ConfirmSkillDialog(context);

        dialog.show();

        return gameParty;
    }

}
