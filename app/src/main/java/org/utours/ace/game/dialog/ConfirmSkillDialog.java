package org.utours.ace.game.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import org.utours.ace.R;

public class ConfirmSkillDialog extends Dialog {

    
    public ConfirmSkillDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_confirm_skill);
    }



}
