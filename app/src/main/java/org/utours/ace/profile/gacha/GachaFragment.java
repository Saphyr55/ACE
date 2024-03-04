package org.utours.ace.profile.gacha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;

import org.utours.ace.R;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.GachaService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GachaFragment extends Fragment {

    private static final int MAX_CARD_CAROUSEL = 5;
    private final GachaService gachaService;
    private final CardService cardService;
    private final Map<String, ImageCard> imageCards;
    private final List<Card> cards;
    private PullConfirmationDialog singlePullConfirmationDialog;
    private PullConfirmationDialog multiPullConfirmationDialog;


    public GachaFragment(GachaService gachaService,
                         CardService cardService) {
        this.gachaService = gachaService;
        this.cardService = cardService;
        this.cards = new ArrayList<>();
        this.imageCards = new HashMap<>();
    }

    public CompletableFuture<Void> setup() {
        return CompletableFuture
                .runAsync(this::onSetup)
                .thenAccept(unused -> cardService
                .provideAllCardImage(this.cards)
                .thenAccept(this::fillCardImages));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Setup
        View fragmentView = inflater.inflate(R.layout.fragment_gacha, container, false);

        // Manage carousel.
        RecyclerView carouselTopCards = fragmentView.findViewById(R.id.carousel_top_cards);
        var gachaCarouselAdapter = new GachaCarouselAdapter(cards, imageCards);
        var carouselLayoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
        var carouselSnapHelper = new CarouselSnapHelper(false);

        // carouselSnapHelper.findSnapView(carouselLayoutManager);
        carouselSnapHelper.attachToRecyclerView(carouselTopCards);

        carouselTopCards.setLayoutManager(carouselLayoutManager);
        carouselTopCards.setAdapter(gachaCarouselAdapter);

        // Manage pull confirmation.
        multiPullConfirmationDialog = new PullConfirmationDialog(fragmentView.getContext());
        singlePullConfirmationDialog = new PullConfirmationDialog(fragmentView.getContext());

        multiPullConfirmationDialog.getPullButton().setOnClickListener(v -> {
            gachaService.multiPull();
            multiPullConfirmationDialog.dismiss();
        });
        singlePullConfirmationDialog.getPullButton().setOnClickListener(v -> {
            gachaService.singlePull();
            singlePullConfirmationDialog.dismiss();
        });

        Button singlePullButton = fragmentView.findViewById(R.id.btn_single_pull);
        Button multiPullButton = fragmentView.findViewById(R.id.btn_multi_pull);

        singlePullButton.setOnClickListener(v -> singlePullConfirmationDialog.show());
        multiPullButton.setOnClickListener(v -> multiPullConfirmationDialog.show());

        return fragmentView;
    }

    private void onSetup() {
        var cards = cardService.getRandomCards(MAX_CARD_CAROUSEL, true);
        this.cards.addAll(cards);
    }

    private void fillCardImages(List<ImageCard> imageCards) {
        var result = new HashMap<String, ImageCard>();
        for (int i = 0; i < imageCards.size(); i++) {
            Card card = cards.get(i);
            result.put(card.pictureId(), imageCards.get(i));
        }
        this.imageCards.putAll(result);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}