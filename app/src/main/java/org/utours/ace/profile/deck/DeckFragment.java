package org.utours.ace.profile.deck;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.utours.ace.R;
import org.utours.ace.infra.profile.PlayerAccountContainer;
import org.utours.ace.profile.NavigationActivity;
import org.utours.ace.profile.dialog.InformationCardDialog;
import org.utours.ace.profile.dialog.SelectClassCoinDialog;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.ImageCardReference;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.ImageCard;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class DeckFragment extends Fragment {

    private ImageCardReference imageCardReference;
    private CardService cardService;
    private PlayerAccountService playerAccountService;
    private GridView gridView;
    private View viewFragment;
    private CardGridAdapter cardGridAdapter;
    private Consumer<CardView> setDefaultCardViewBackground;
    private Consumer<CardView> setSelectedBackgroundColor;
    private Card cardSelected;
    private CardView cardViewSelected;
    private TextView attackPointText;
    private TextView manaPointText;
    private TextView lifePointText;
    private Button classButton;
    private Button informationButton;
    private Context context;
    private NavigationActivity navigationActivity;

    public static DeckFragment newInstance(CardService cardService,
                                           PlayerAccountService playerAccountService,
                                           NavigationActivity navigationActivity) {
        DeckFragment deckFragment = new DeckFragment();
        deckFragment.context = navigationActivity;
        deckFragment.navigationActivity = navigationActivity;
        deckFragment.cardService = cardService;
        deckFragment.playerAccountService = playerAccountService;
        deckFragment.setDefaultCardViewBackground = (cardView) -> {
            var theme = deckFragment.viewFragment.getContext().getTheme();
            cardView.setBackgroundColor(deckFragment.getResources().getColor(R.color.white, theme));
        };
        deckFragment.setSelectedBackgroundColor = (cardView) -> {
            int color = Color.rgb(255, 187, 156);
            cardView.setBackgroundColor(color);
        };
        deckFragment.imageCardReference = ImageCardReference.create(cardService);
        return deckFragment;
    }

    public DeckFragment() { }

    public CompletableFuture<Void> setup() {
        return imageCardReference
                .provideImageCards(playerAccountService.currentPlayerAccount().cards())
                .thenAccept(stringImageCardMap -> {});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewFragment = view;

        AppCompatToggleButton deckToggleButton = viewFragment.findViewById(R.id.toggle_deck);
        gridView = viewFragment.findViewById(R.id.grid_cards);
        attackPointText = viewFragment.findViewById(R.id.text_attackPoint);
        manaPointText = viewFragment.findViewById(R.id.text_manaCost);
        lifePointText = viewFragment.findViewById(R.id.text_lifePoint);

        deckToggleButton.setOnCheckedChangeListener(this::onToggleDeck);
        var playerAccount = playerAccountService.currentPlayerAccount();
        cardGridAdapter = new CardGridAdapter(getContext(),
                cardService.sortedByRarity(playerAccount.cards()),
                imageCardReference,
                this::onCreateCardViewCardsMode);

        classButton = viewFragment.findViewById(R.id.btn_class);
        informationButton = viewFragment.findViewById(R.id.btn_information);
        classButton.setEnabled(false);
        informationButton.setEnabled(false);
        classButton.setOnClickListener(v -> onClickClassButton());
        informationButton.setOnClickListener(v -> onClickInfoButton());

        setup().thenRun(() -> {
            gridView.setAdapter(cardGridAdapter);
            setupCardDetailsOnSelect(null);
        });
    }

    private void onClickInfoButton() {
        Dialog dialog = new InformationCardDialog(
                navigationActivity,
                cardSelected,
                imageCardReference.get(cardSelected),
                PlayerAccountContainer.INSTANCE.getInformationCardProvider());
        dialog.show();
    }

    private void onToggleDeck(CompoundButton compoundButton, boolean isActive) {
        var playerAccount = playerAccountService.currentPlayerAccount();
        if (isActive) {
            cardGridAdapter.setOnCreateCardView(this::onCreateCardViewDeckMode);
            cardGridAdapter.setCards(cardService.cardsDeckFirst(playerAccount));
        } else {
            cardGridAdapter.setOnCreateCardView(this::onCreateCardViewCardsMode);
            cardGridAdapter.setCards(cardService.sortedByRarity(playerAccount.cards()));
        }
        // Save data on quiting deck mode.
        playerAccountService.updateCurrentPlayerAccount();

        cardGridAdapter.notifyDataSetChanged();
    }

    private void onCreateCardViewDeckMode(CardView cardView, Card card) {

        classButton.setEnabled(false);
        informationButton.setEnabled(false);

        var playerAccount = playerAccountService.currentPlayerAccount();
        var selectedCardIsInDeck = playerAccount.deck().cards().contains(card);

        if (selectedCardIsInDeck) {
            setSelectedBackgroundColor.accept(cardView);
            cardView.setOnClickListener(v -> {
                playerAccountService.setCurrentPlayerAccount(pa -> pa.removeCardInDeck(card));
                cardGridAdapter.notifyDataSetChanged();
            });
        } else {
            setDefaultCardViewBackground.accept(cardView);
            cardView.setOnClickListener(v -> {
                if (playerAccount.deck().cards().size() > Deck.MAX_SLOT) return;
                playerAccountService.setCurrentPlayerAccount(pa -> pa.addCardInDeck(card));
                cardGridAdapter.notifyDataSetChanged();
            });
        }
    }

    private void onCreateCardViewCardsMode(CardView cardView, Card card) {

        setDefaultCardViewBackground.accept(cardView);
        cardView.setOnClickListener(v -> {

            boolean isEnabled = cardSelected == null;
            classButton.setEnabled(isEnabled);
            informationButton.setEnabled(isEnabled);

            if (cardSelected == null) {
                cardSelected = card;
                cardViewSelected = cardView;
                setSelectedBackgroundColor.accept(cardViewSelected);
                setupCardDetailsOnSelect(card);
                return;
            }

            isEnabled = !card.equals(cardSelected);
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

            classButton.setEnabled(isEnabled);
            informationButton.setEnabled(isEnabled);

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
            bindCardView(selectedViewCard, card, (cardView, card1) -> { }, imageCardReference);
            attackPoint = String.valueOf(card.attackPoint());
            manaPoint = String.valueOf(card.manaPoint());
            lifePoint = String.valueOf(card.lifePoint());
        }

        attackPointText.setText(attackPoint);
        manaPointText.setText(manaPoint);
        lifePointText.setText(lifePoint);
    }

    public static void bindCardView(View view, Card card,
                                    BiConsumer<CardView, Card> cardBiConsumer,
                                    ImageCardReference imageCardReference) {

        CardView cardView = view.findViewById(R.id.card_card);
        ImageView iconImageView = view.findViewById(R.id.image_card);
        TextView nameTextView = view.findViewById(R.id.text_nameCard);
        TextView rarityTextView = view.findViewById(R.id.text_rarity);

        cardBiConsumer.accept(cardView, card);

        ImageCard imageCard = imageCardReference.get(card);
        assert imageCard != null;
        byte[] content = imageCard.content();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);

        iconImageView.setImageBitmap(bitmap);
        nameTextView.setText(card.name());
        rarityTextView.setText(card.rarity().name());
    }

    private void onClickClassButton() {
        if (cardSelected != null) {
            var dialog = new SelectClassCoinDialog(context, playerAccountService, cardSelected);
            dialog.show();
        }
    }

}