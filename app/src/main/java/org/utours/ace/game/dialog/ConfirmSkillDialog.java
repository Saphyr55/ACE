package org.utours.ace.game.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import org.utours.ace.R;
import org.utours.ace.game.CardViewBinding;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

public class ConfirmSkillDialog extends Dialog {

    private final Button cancelButton;
    private final Button activateSkill;
    private final Button attackButton;
    private final FrameLayout cardFromLayout;
    private final FrameLayout cardToLayout;

    public ConfirmSkillDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_confirm_skill);
        cancelButton = findViewById(R.id.btn_cancel);
        activateSkill = findViewById(R.id.btn_activateSkill);
        attackButton = findViewById(R.id.btn_attack);
        cardFromLayout = findViewById(R.id.layout_cardFrom);
        cardToLayout = findViewById(R.id.layout_cardTo);
    }

    public void setCardFrom(Card card, ImageCard imageCard) {
        CardViewBinding cardViewBinding = new CardViewBinding(cardFromLayout);
        cardViewBinding.bind(card, imageCard);
    }

    public void setCardTo(Card card, ImageCard imageCard) {
        CardViewBinding cardViewBinding = new CardViewBinding(cardToLayout);
        cardViewBinding.bind(card, imageCard);
    }

    public void onAttackButton(Runnable runnable) {
        attackButton.setOnClickListener(v -> runnable.run());
    }

    public void onActivateSkillButton(Runnable runnable) {
        activateSkill.setOnClickListener(v -> runnable.run());
    }

    public void onCancelButton(Runnable runnable) {
        cancelButton.setOnClickListener(v -> runnable.run());
    }

    public Button getAttackButton() {
        return attackButton;
    }

    public Button getActivateSkill() {
        return activateSkill;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
