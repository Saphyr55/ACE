package org.utours.ace.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class DeckFragment extends Fragment {

    private final CardService cardService;
    private final PlayerAccountService playerAccountService;
    private final Map<String, ImageCard> cardImages;
    private GridView gridView;
    private View viewFragment;
    private CardGridAdapter cardGridAdapter;
    private AppCompatToggleButton deckToggleButton;
    private BiConsumer<CardView, Card> defaultOnCreateCardView;


    public DeckFragment(CardService cardService,
                        PlayerAccountService playerAccountService) {

        this.cardService = cardService;
        this.playerAccountService = playerAccountService;
        this.cardImages = new HashMap<>();
    }

    public CompletableFuture<Void> setup() {
        return cardService
                .provideAllCardImage(playerAccountService.currentPlayerAccount())
                .thenAccept(this::fillCardImages);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        viewFragment = inflater.inflate(R.layout.fragment_deck, container, false);
        gridView = viewFragment.findViewById(R.id.grid_cards);
        deckToggleButton = viewFragment.findViewById(R.id.toggle_deck);

        defaultOnCreateCardView = (cardView, card) -> {
            var theme = viewFragment.getContext().getTheme();
            cardView.setBackgroundColor(getResources().getColor(R.color.white, theme));
        };

        deckToggleButton.setOnCheckedChangeListener(this::onToggleDeck);

        var playerAccount = playerAccountService.currentPlayerAccount();
        cardGridAdapter = new CardGridAdapter(getContext(),
                cardService.sortedByRarity(playerAccount.cards()),
                cardImages,
                defaultOnCreateCardView);

        gridView.setAdapter(cardGridAdapter);

        return viewFragment;
    }

    private void onToggleDeck(CompoundButton compoundButton, boolean isActive) {
        var playerAccount = playerAccountService.currentPlayerAccount();

        if (isActive) {
            cardGridAdapter.setOnCreateCardView(this::onCreateCardView);
            cardGridAdapter.setCards(cardService.cardsDeckFirst(playerAccount));
        } else {
            cardGridAdapter.setOnCreateCardView(defaultOnCreateCardView);
            cardGridAdapter.setCards(cardService.sortedByRarity(playerAccount.cards()));
            playerAccountService.updateCurrentPlayerAccount();
        }

        cardGridAdapter.notifyDataSetChanged();
    }

    private void onCreateCardView(CardView cardView, Card card) {

        var playerAccount = playerAccountService.currentPlayerAccount();

        Consumer<CardView> setBackgroundColor = cd -> {
            int color = Color.rgb(255, 187, 156);
            cd.setBackgroundColor(color);
        };

        if (playerAccount.deck().cards().contains(card)) {

            setBackgroundColor.accept(cardView);

            cardView.setOnClickListener(v -> {
                playerAccountService.updateCurrentPlayerAccount(pa -> pa.removeCardInDeck(card));
                cardGridAdapter.notifyDataSetChanged();
            });

        } else {

            defaultOnCreateCardView.accept(cardView, card);

            cardView.setOnClickListener(v -> {
                playerAccountService.updateCurrentPlayerAccount(pa -> pa.addCardInDeck(card));
                cardGridAdapter.notifyDataSetChanged();
            });
        }

    }

    public static void setupCardView(View view, Card card,
                                     BiConsumer<CardView, Card> cardBiConsumer,
                                     Map<String, ImageCard> cardImages) {

        CardView cardView = view.findViewById(R.id.card_card);
        ImageView iconImageView = view.findViewById(R.id.image_card);
        TextView nameTextView = view.findViewById(R.id.text_nameCard);
        TextView rarityTextView = view.findViewById(R.id.text_rarity);

        cardBiConsumer.accept(cardView, card);

        ImageCard imageCard = cardImages.get(card.pictureAddress());
        assert imageCard != null;
        byte[] content = imageCard.content();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);

        iconImageView.setImageBitmap(bitmap);
        nameTextView.setText(card.name());
        rarityTextView.setText(card.rarity().name());
    }

    private void fillCardImages(List<ImageCard> imageCards) {
        var result = new HashMap<String, ImageCard>();
        for (int i = 0; i < imageCards.size(); i++) {
            Card card = playerAccountService.currentPlayerAccount().cards().get(i);
            result.put(card.pictureAddress(), imageCards.get(i));
        }
        this.cardImages.putAll(result);
    }

}