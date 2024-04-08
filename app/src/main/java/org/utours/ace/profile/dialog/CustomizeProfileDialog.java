package org.utours.ace.profile.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import org.utours.ace.R;
import org.utours.ace.profile.deck.CardGridAdapter;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.ImageCardReference;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.PlayerAccount;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class CustomizeProfileDialog extends Dialog {


    public static final int REQUEST_CODE = 1001;
    private final ImageCardReference imageCardReference;
    private final Consumer<View> setDefaultCardViewBackground;
    private final Consumer<View> setSelectedBackgroundColor;
    private NavigationActivity navigationActivity;
    private PlayerAccountService pAccountService;
    private CardGridAdapter cardGridAdapter;
    private Card cardSelected;
    private CardView cardViewSelected;

    public CustomizeProfileDialog(@NonNull NavigationActivity navigationActivity,
                                  PlayerAccountService playerAccountService,
                                  CardService cardService,
                                  ImageCardReference imageCardReference,
                                  BiConsumer<CardView,Card> refreshable) {

        super(navigationActivity);
        this.imageCardReference = imageCardReference;
        pAccountService = playerAccountService;
        this.navigationActivity = navigationActivity;
        setContentView(R.layout.dialog_customize_profile);

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        Button uploadButton = findViewById(R.id.btn_uploadImage);
        Button cancelButton = findViewById(R.id.btn_cancel);
        Button saveButton = findViewById(R.id.btn_save);
        EditText descriptionEditText = findViewById(R.id.edittext_description);
        GridView gridView = findViewById(R.id.grid_cards);

        descriptionEditText.setText(pAccountService.currentPlayerAccount().description());

        cardGridAdapter = new CardGridAdapter(
                getContext(),
                List.copyOf(playerAccountService.currentPlayerAccount().cards()),
                this.imageCardReference,
                this::onCreateCardViewCardsMode);

        gridView.setAdapter(cardGridAdapter);

        setDefaultCardViewBackground = (cardView) -> {
            var theme = getContext().getTheme();
            cardView.setBackgroundColor(getContext().getResources().getColor(R.color.white, theme));
        };

        setSelectedBackgroundColor = (cardView) -> {
            int color = Color.rgb(255, 187, 156);
            cardView.setBackgroundColor(color);
        };

        uploadButton.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_GET_CONTENT);
            iGallery.setType("image/*");
            navigationActivity.startActivityForResult(iGallery, REQUEST_CODE);
        });



        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString();
            playerAccountService.setCurrentPlayerAccount(playerAccount -> {
                return PlayerAccount.Builder.copyOf(playerAccount)
                        .withDescription(description)
                        .build();
            });
            playerAccountService.updateCurrentPlayerAccount();
            refreshable.accept(cardViewSelected, cardSelected);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

    }



    private void onCreateCardViewCardsMode(CardView cardView, Card card) {

        setDefaultCardViewBackground.accept(cardView);
        cardView.setOnClickListener(v -> {

            if (cardSelected == null) {
                cardSelected = card;
                cardViewSelected = cardView;
                setSelectedBackgroundColor.accept(cardViewSelected);
                return;
            }

            if (card.equals(cardSelected)) {
                cardSelected = null;
                cardViewSelected = null;
                setDefaultCardViewBackground.accept(cardView);
            } else {
                setDefaultCardViewBackground.accept(cardViewSelected);
                cardSelected = card;
                cardViewSelected = cardView;
                setSelectedBackgroundColor.accept(cardViewSelected);
            }
        });
    }

}
