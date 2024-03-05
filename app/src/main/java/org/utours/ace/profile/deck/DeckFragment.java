package org.utours.ace.profile.deck;

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
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
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
    private Consumer<CardView> setDefaultCardViewBackground;
    private Consumer<CardView> setSelectedBackgroundColor;
    private Card cardSelected;
    private CardView cardViewSelected;
    private TextView attackPointText;
    private TextView manaPointText;
    private TextView lifePointText;

    public DeckFragment(CardService cardService,
                        PlayerAccountService playerAccountService) {

        this.cardService = cardService;
        this.playerAccountService = playerAccountService;
        this.cardImages = new HashMap<>();
        this.setDefaultCardViewBackground = (cardView) -> {
            var theme = viewFragment.getContext().getTheme();
            cardView.setBackgroundColor(getResources().getColor(R.color.white, theme));
        };
        this.setSelectedBackgroundColor = (cardView) -> {
            int color = Color.rgb(255, 187, 156);
            cardView.setBackgroundColor(color);
        };
    }

    public CompletableFuture<Void> setup() {
        return cardService
                .provideImageCards(playerAccountService.currentPlayerAccount().cards())
                .thenAccept(this::fillCardImages);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        viewFragment = inflater.inflate(R.layout.fragment_deck, container, false);

        deckToggleButton = viewFragment.findViewById(R.id.toggle_deck);
        gridView = viewFragment.findViewById(R.id.grid_cards);
        attackPointText = viewFragment.findViewById(R.id.text_attackPoint);
        manaPointText = viewFragment.findViewById(R.id.text_manaCost);
        lifePointText = viewFragment.findViewById(R.id.text_lifePoint);

        deckToggleButton.setOnCheckedChangeListener(this::onToggleDeck);

        var playerAccount = playerAccountService.currentPlayerAccount();
        cardGridAdapter = new CardGridAdapter(
                getContext(),
                cardService.sortedByRarity(playerAccount.cards()),
                cardImages,
                this::onCreateCardViewCardsMode);

        gridView.setAdapter(cardGridAdapter);
        setupCardDetailsOnSelect(null);

        return viewFragment;
    }

    private void onToggleDeck(CompoundButton compoundButton, boolean isActive) {
        var playerAccount = playerAccountService.currentPlayerAccount();
        if (isActive) {
            cardGridAdapter.setOnCreateCardView(this::onCreateCardViewDeckMode);
            cardGridAdapter.setCards(cardService.cardsDeckFirst(playerAccount));
        } else {
            cardGridAdapter.setOnCreateCardView(this::onCreateCardViewCardsMode);
            cardGridAdapter.setCards(cardService.sortedByRarity(playerAccount.cards()));

            // Save data on quiting deck mode.
            playerAccountService.storeCurrentPlayerAccount();
        }
        cardGridAdapter.notifyDataSetChanged();
    }

    private void onCreateCardViewDeckMode(CardView cardView, Card card) {
        var playerAccount = playerAccountService.currentPlayerAccount();
        var selectedCardIsInDeck = playerAccount.deck().cards().contains(card);
        if (selectedCardIsInDeck) {
            setSelectedBackgroundColor.accept(cardView);
            cardView.setOnClickListener(v -> {
                playerAccountService.updateCurrentPlayerAccount(pa -> pa.removeCardInDeck(card));
                cardGridAdapter.notifyDataSetChanged();
            });
        } else {
            setDefaultCardViewBackground.accept(cardView);
            cardView.setOnClickListener(v -> {
                if (playerAccount.deck().cards().size() > Deck.MAX_SLOT) return;
                playerAccountService.updateCurrentPlayerAccount(pa -> pa.addCardInDeck(card));
                cardGridAdapter.notifyDataSetChanged();
            });
        }
    }

    private void onCreateCardViewCardsMode(CardView cardView, Card card) {

        setDefaultCardViewBackground.accept(cardView);
        cardView.setOnClickListener(v -> {

            if (cardSelected == null) {
                cardSelected = card;
                cardViewSelected = cardView;
                setSelectedBackgroundColor.accept(cardViewSelected);
                setupCardDetailsOnSelect(card);
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

            setupCardDetailsOnSelect(cardSelected);
        });
    }

    private void setupCardDetailsOnSelect(Card card) {

        CardView selectedViewCard = viewFragment.findViewById(R.id.card_selectedCard);
        String attackPoint = getResources().getString(R.string.unknown);
        String manaPoint = getResources().getString(R.string.unknown);
        String lifePoint = getResources().getString(R.string.unknown);

        if (card == null) {

            ImageView iconImageView = selectedViewCard.findViewById(R.id.image_card);
            TextView nameTextView = selectedViewCard.findViewById(R.id.text_nameCard);
            TextView rarityTextView = selectedViewCard.findViewById(R.id.text_rarity);

            iconImageView.setImageDrawable(null);
            var theme = viewFragment.getContext().getTheme();
            selectedViewCard.setBackgroundColor(getResources().getColor(R.color.light_gray, theme));
            nameTextView.setText(R.string.card_name);
            rarityTextView.setText(R.string.rarity);

        } else {
            setupCardView(selectedViewCard, card, (cardView, card1) -> {}, cardImages);
            attackPoint = String.valueOf(card.attackPoint());
            manaPoint = String.valueOf(card.manaPoint());
            lifePoint = String.valueOf(card.lifePoint());
        }

        attackPointText.setText(attackPoint);
        manaPointText.setText(manaPoint);
        lifePointText.setText(lifePoint);
    }

    public static void setupCardView(View view, Card card,
                                     BiConsumer<CardView, Card> cardBiConsumer,
                                     Map<String, ImageCard> cardImages) {

        CardView cardView = view.findViewById(R.id.card_card);
        ImageView iconImageView = view.findViewById(R.id.image_card);
        TextView nameTextView = view.findViewById(R.id.text_nameCard);
        TextView rarityTextView = view.findViewById(R.id.text_rarity);

        cardBiConsumer.accept(cardView, card);

        ImageCard imageCard = cardImages.get(card.pictureId());
        assert imageCard != null;
        byte[] content = imageCard.content();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);

        iconImageView.setImageBitmap(bitmap);
        nameTextView.setText(card.name());
        rarityTextView.setText(card.rarity().name());
    }

    private void fillCardImages(List<ImageCard> imageCards) {
        var result = new HashMap<String, ImageCard>();
        var list = List.copyOf(playerAccountService.currentPlayerAccount().cards());
        for (int i = 0; i < imageCards.size(); i++) {
            Card card = list.get(i);
            result.put(card.pictureId(), imageCards.get(i));
        }
        this.cardImages.putAll(result);
    }

}