package org.utours.ace.profile.gacha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.CarouselStrategy;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;

import org.utours.ace.R;
import org.utours.ace.profile.feature.CardRegistry;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class GachaFragment extends Fragment {

    private static final int MAX_CARD_CAROUSEL = 5;
    private final CardService cardService;
    private final Map<String, ImageCard> imageCards;
    private final List<Card> cards;

    public GachaFragment(CardService cardService) {
        this.cardService = cardService;
        this.cards = new ArrayList<>();
        this.imageCards = new HashMap<>();
    }

    public CompletableFuture<Void> setup() {
        var cards = cardService.getRandomCards(MAX_CARD_CAROUSEL);
        this.cards.addAll(cards);
        return cardService
                .provideAllCardImage(this.cards)
                .thenAccept(this::fillCardImages);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_gacha, container, false);

        RecyclerView carouselTopCards = fragmentView.findViewById(R.id.carousel_top_cards);

        GachaCarouselAdapter gachaCarouselAdapter = new GachaCarouselAdapter(cards, imageCards);
        CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());

        CarouselSnapHelper carouselSnapHelper = new CarouselSnapHelper(false);
        carouselSnapHelper.findSnapView(carouselLayoutManager);
        carouselSnapHelper.attachToRecyclerView(carouselTopCards);

        carouselTopCards.setLayoutManager(carouselLayoutManager);
        carouselTopCards.setAdapter(gachaCarouselAdapter);

        return fragmentView;
    }

    private void fillCardImages(List<ImageCard> imageCards) {
        var result = new HashMap<String, ImageCard>();
        for (int i = 0; i < imageCards.size(); i++) {
            Card card = cards.get(i);
            result.put(card.pictureAddress(), imageCards.get(i));
        }
        this.imageCards.putAll(result);
    }

}