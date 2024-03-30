package org.utours.ace.profile.feature.impl;

import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.ImageCardReference;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ImageCardReferenceImpl implements ImageCardReference {

    private final CardService cardService;
    private Map<String, ImageCard> imageCardsReferences;

    public ImageCardReferenceImpl(CardService cardService) {
        this.cardService = cardService;
        this.imageCardsReferences = new HashMap<>();
    }

    @Override
    public CompletableFuture<Map<String, ImageCard>> provideImageCards(Collection<Card> cards) {
        return cardService.provideImageCards(cards)
                .thenApply(imageCards -> setImageCardReferences(new ArrayList<>(cards), imageCards));
    }

    private Map<String, ImageCard> setImageCardReferences(List<Card> cards, List<ImageCard> imageCards) {
        imageCardsReferences = cardService.getMapReferences(cards, imageCards);
        return imageCardsReferences;
    }


    @Override
    public Map<String, ImageCard> getReferences() {
        return imageCardsReferences;
    }

}
