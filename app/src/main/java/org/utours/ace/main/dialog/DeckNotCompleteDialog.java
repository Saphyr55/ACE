package org.utours.ace.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

import org.utours.ace.R;
import org.utours.ace.profile.NavigationActivity;

import java.util.Optional;

public final class DeckNotCompleteDialog extends Dialog {

    public DeckNotCompleteDialog(@NonNull Context context,
                                 NavigationActivity activity) {
        super(context);
        setContentView(R.layout.dialog_deck_not_complet);

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        Button backButton = findViewById(R.id.btn_cancel);
        Button goToDeckButton = findViewById(R.id.btn_confirm);

        goToDeckButton.setOnClickListener(v -> {
            dismiss();
            activity.redirectToDeck();
        });

        backButton.setOnClickListener(v -> dismiss());
    }


    public static DeckNotCompleteDialog createAndShow(Context context, NavigationActivity activity) {
        var d = new DeckNotCompleteDialog(context, activity);
        d.show();
        return d;
    }

}
