package org.utours.ace.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ProfileCard;
import org.utours.ace.profile.model.ImageProfileCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public final class DeckFragment extends Fragment {

    private final PlayerAccountService playerAccountService;
    private final PlayerAccount playerAccount;
    private final Map<String, ImageProfileCard> cardImages;

    public DeckFragment(PlayerAccountService playerAccountService,
                        PlayerAccount playerAccount) {

        this.playerAccountService = playerAccountService;
        this.playerAccount = playerAccount;
        this.cardImages = new HashMap<>();
    }

    public CompletableFuture<Void> setup() {
        return playerAccountService
                .provideAllCardImage(playerAccount)
                .thenAccept(cardImages::putAll);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deck, container, false);
        GridView gridView = view.findViewById(R.id.grid_cards);

        CardView deckFirstCardView = view.findViewById(R.id.view_first_card);
        CardView deckSecondCardView = view.findViewById(R.id.view_second_card);
        CardView deckThirdCardView = view.findViewById(R.id.view_third_card);
        CardView deckFourthCardView = view.findViewById(R.id.view_fourth_card);
        CardView deckFifthCardView = view.findViewById(R.id.view_fifth_card);

        setupCardView(deckFirstCardView, playerAccount.deck().first(), cardImages);
        setupCardView(deckSecondCardView, playerAccount.deck().second(), cardImages);
        setupCardView(deckThirdCardView, playerAccount.deck().third(), cardImages);
        setupCardView(deckFourthCardView, playerAccount.deck().fourth(), cardImages);
        setupCardView(deckFifthCardView, playerAccount.deck().fifth(), cardImages);

        var cardGridAdapter = new CardGridAdapter(getContext(), playerAccount.cards(), cardImages);
        gridView.setAdapter(cardGridAdapter);

        return view;
    }

    public static void setupCardView(View view,
                                     ProfileCard card,
                                     Map<String, ImageProfileCard> cardImages) {

        ImageView iconImageView = view.findViewById(R.id.image_card);
        TextView nameTextView = view.findViewById(R.id.text_nameCard);
        // TextView attackPointTextView = view.findViewById(R.id.text_attackPointCard);
        // TextView lifePointTextView = view.findViewById(R.id.text_lifePointCard);
        // TextView manaPointTextView = view.findViewById(R.id.text_manaPointCard);
        TextView rarityTextView = view.findViewById(R.id.text_rarity);

        ImageProfileCard imageProfileCard = cardImages.get(card.pictureAddress());
        assert imageProfileCard != null;
        byte[] content = imageProfileCard.content();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);

        iconImageView.setImageBitmap(bitmap);
        nameTextView.setText(card.name());
        // attackPointTextView.setText(String.valueOf(card.attackPoint()));
        // lifePointTextView.setText(String.valueOf(card.lifePoint()));
        // manaPointTextView.setText(String.valueOf(card.manaPoint()));
        rarityTextView.setText(card.rarity().name());
    }

}