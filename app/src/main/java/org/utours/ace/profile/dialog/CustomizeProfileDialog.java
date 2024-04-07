package org.utours.ace.profile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import org.utours.ace.R;
import org.utours.ace.common.Refreshable;
import org.utours.ace.profile.CardGridAdapter;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.model.PlayerAccount;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class CustomizeProfileDialog extends Dialog {

    private final Map<String, ImageCard> cardImages;
    private final Consumer<View> setDefaultCardViewBackground;
    private final Consumer<View> setSelectedBackgroundColor;
    private PlayerAccountService pAccountService;
    private CardGridAdapter cardGridAdapter;
    private Card cardSelected;
    private CardView cardViewSelected;

    public CustomizeProfileDialog(@NonNull NavigationActivity navigationActivity,
                                  PlayerAccountService playerAccountService,
                                  CardService cardService,
                                  Map<String, ImageCard> cardImages,
                                  BiConsumer<CardView,Card> refreshable) {

        super(navigationActivity);
        this.cardImages = cardImages;
        pAccountService = playerAccountService;

        setContentView(R.layout.dialog_customize_profile);

        Optional.ofNullable(getWindow())
                .ifPresent(window -> window.setWindowAnimations(R.style.DialogAnimations));

        Button uploadButton = findViewById(R.id.btn_uploadImage);
        Button photoButton = findViewById(R.id.btn_takePhoto);
        Button cancelButton = findViewById(R.id.btn_cancel);
        Button saveButton = findViewById(R.id.btn_save);
        EditText descriptionEditText = findViewById(R.id.edittext_description);
        GridView gridView = findViewById(R.id.grid_cards);

        descriptionEditText.setText(pAccountService.currentPlayerAccount().description());

        cardGridAdapter = new CardGridAdapter(
                getContext(),
                List.copyOf(playerAccountService.currentPlayerAccount().cards()),
                this.cardImages,
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
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            navigationActivity.startActivity(iGallery);
        });

        photoButton.setOnClickListener(v -> {

        });

        saveButton.setOnClickListener(v -> {
            String description = descriptionEditText.getText().toString();
            playerAccountService.setCurrentPlayerAccount(playerAccount -> {
                return PlayerAccount.Builder.of(playerAccount)
                        .withDescription(description)
                        .build();
            });
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
