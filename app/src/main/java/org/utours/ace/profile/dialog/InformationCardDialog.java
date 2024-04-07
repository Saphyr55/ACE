package org.utours.ace.profile.dialog;

import android.app.Dialog;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.game.CardViewBinding;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.InformationCardProvider;

import java.util.Optional;

public class InformationCardDialog extends Dialog {


    public InformationCardDialog(NavigationActivity navigationActivity,
                                 Card card,
                                 ImageCard imageCard,
                                 InformationCardProvider informationCardDialog) {
        super(navigationActivity);
        setContentView(R.layout.dialog_information_card);

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        TextView informationTextView = findViewById(R.id.text_information);
        FrameLayout cardView = findViewById(R.id.card_card);

        CardViewBinding cardViewBinding = new CardViewBinding(cardView);
        cardViewBinding.bind(card, imageCard);

        informationCardDialog
                .information(card)
                .thenAccept(s -> setTextInformation(informationTextView, navigationActivity, s));
    }

    void setTextInformation(TextView informationTextView, NavigationActivity navigationActivity, String information) {
        navigationActivity.runOnUiThread(() -> informationTextView.setText(information));
    }

}
