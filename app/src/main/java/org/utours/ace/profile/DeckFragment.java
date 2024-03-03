package org.utours.ace.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

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
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


public final class DeckFragment extends Fragment {

    private final CardService cardService;
    private final PlayerAccount playerAccount;
    private final Map<String, ImageCard> cardImages;
    private GridView gridView;
    private View viewFragment;
    private CardGridAdapter cardGridAdapter;
    private AppCompatToggleButton deckToggleButton;
    private BiConsumer<CardView, Card> defaultOnCreateCardView;


    public DeckFragment(CardService cardService,
                        PlayerAccount playerAccount) {

        this.cardService = cardService;
        this.playerAccount = playerAccount;
        this.cardImages = new HashMap<>();
    }

    public CompletableFuture<Void> setup() {
        return cardService
                .provideAllCardImage(playerAccount)
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

        cardGridAdapter = new CardGridAdapter(getContext(),
                cardService.sortedByRarity(playerAccount.cards()),
                playerAccount.deck(), cardImages, defaultOnCreateCardView);

        gridView.setAdapter(cardGridAdapter);

        return viewFragment;
    }

    private void onToggleDeck(CompoundButton compoundButton, boolean isActive) {
        if (isActive) {
            cardGridAdapter.setBitmapBiFunction((cardView, card) -> {
                if (playerAccount.deck().cards().contains(card)) {
                    int color = Color.rgb(255, 187, 156);
                    cardView.setBackgroundColor(color);
                }
            });
            cardGridAdapter.setCards(cardService.cardsDeckFirst(playerAccount));
        } else {
            cardGridAdapter.setBitmapBiFunction(defaultOnCreateCardView);
            cardGridAdapter.setCards(cardService.sortedByRarity(playerAccount.cards()));
        }
        cardGridAdapter.notifyDataSetChanged();
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
            String id = playerAccount.cards().get(i).pictureAddress();
            result.put(id, imageCards.get(i));
        }
        this.cardImages.putAll(result);
    }

}