package org.utours.ace.common;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

import org.utours.ace.R;

import java.util.Optional;

public class BackConfirmDialog extends Dialog {

    private final Button cancelButton;
    private final Button confirmButton;

    public BackConfirmDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_back_confirm);

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        this.cancelButton = findViewById(R.id.btn_cancel);
        this.confirmButton = findViewById(R.id.btn_confirm);

        this.cancelButton.setOnClickListener(v -> dismiss());
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }


    public void setOnConfirmButton(Runnable runnable) {
        confirmButton.setOnClickListener(v -> runnable.run());
    }

}
