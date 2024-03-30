package org.utours.ace.profile.feature;

import org.utours.ace.profile.feature.impl.ImageCardReferenceImpl;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ImageCardReference {

    static ImageCardReference create(CardService cardService) {
        return new ImageCardReferenceImpl(cardService);
    }

    CompletableFuture<Map<String, ImageCard>> provideImageCards(Collection<Card> cards);

    Map<String, ImageCard> getReferences();

    default ImageCard get(Card card) {
        ImageCard imageCard = getReferences()
                .get(card.pictureId());
        assert imageCard != null;
        return imageCard;
    }

}
