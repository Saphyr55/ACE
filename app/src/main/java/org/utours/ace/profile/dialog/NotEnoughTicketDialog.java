package org.utours.ace.profile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

import org.utours.ace.R;
import org.utours.ace.profile.NavigationActivity;

import java.util.Optional;

public final class NotEnoughTicketDialog extends Dialog {

    public NotEnoughTicketDialog(@NonNull Context context, NavigationActivity activity) {
        super(context);
        setContentView(R.layout.dialog_not_enough_ticket);

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        Button backButton = findViewById(R.id.btn_cancel);
        Button goToTheShopButton = findViewById(R.id.btn_confirm);

        goToTheShopButton.setOnClickListener(v -> {
            dismiss();
            activity.redirectToShop();
        });

        backButton.setOnClickListener(v -> dismiss());
    }


}
