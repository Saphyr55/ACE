package org.utours.ace.profile.gacha;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

import org.utours.ace.R;

import java.util.Optional;

public class PullConfirmationDialog extends Dialog {

    private final Button cancelButton;
    private final Button pullButton;

    public PullConfirmationDialog(@NonNull Context context) {

        super(context);
        setContentView(R.layout.dialog_pull_confirmation);
        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        this.cancelButton = findViewById(R.id.btn_cancel);
        this.pullButton = findViewById(R.id.btn_pull);

        this.cancelButton.setOnClickListener(v -> dismiss());
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getPullButton() {
        return pullButton;
    }



}
