package org.utours.ace.profile.feature;

import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public enum CardRegistry {
    INSTANCE;

    /**
     * Fern
     */
    public static final Supplier<Card> FERN_CARD = INSTANCE.register(() -> new Card("7", "Fern",
            "https://cdn.myanimelist.net/images/characters/13/519083.jpg",
            20,
            10,
            5,
            CardRarity.SSR));

    /**
     *  Ubel
     */
    public static final Supplier<Card> UBEL_CARD = INSTANCE.register(() -> new Card("6", "Übel",
            "https://cdn.myanimelist.net/images/characters/2/535522.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    /**
     * Frieren
     */
    public static final Supplier<Card> FRIEREN_CARD = INSTANCE.register(() -> new Card("5", "Frieren",
            "https://cdn.myanimelist.net/images/characters/4/420348.jpg",
            20,
            10,
            5,
            CardRarity.UR));

    /**
     * Stark
     */
    public static final Supplier<Card> STARK_CARD = INSTANCE.register(() ->
            Card.Builder.newBuilder()
                    .withName("Stark")
                    .withId("4")
                    .withPictureId("https://cdn.myanimelist.net/images/characters/3/423034.jpg")
                    .withRarity(CardRarity.SSR)
                    .withManaPoint(5)
                    .withLifePoint(20)
                    .withAttackPoint(10)
                    .build());

    /**
     * Himmel
     */
    public static final Supplier<Card> HIMMEL_CARD = INSTANCE.register(() -> new Card("3", "Himmel",
            "https://cdn.myanimelist.net/images/characters/6/471527.jpg",
            20,
            10,
            5,
            CardRarity.SSR));

    /**
     * Eisen
     */
    public static final Supplier<Card> EISEN_CARD = INSTANCE.register(() -> new Card("2", "Eisen",
            "https://cdn.myanimelist.net/images/characters/12/471526.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    /**
     *  Heiter
     */
    public static final Supplier<Card> HEITER_CARD = INSTANCE.register(() -> new Card("1", "Heiter",
            "https://cdn.myanimelist.net/images/characters/9/471528.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    /**
     * Methode
     */
    public static final Supplier<Card> METHODE_CARD = INSTANCE.register(() -> new Card("8", "Methode",
            "https://cdn.myanimelist.net/images/characters/14/538692.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    /**
     * Sein
     */
    public static final Supplier<Card> SEIN_CARD = INSTANCE.register(() -> new Card("9", "Sein",
            "https://cdn.myanimelist.net/images/characters/3/442553.jpg",
            20,
            10,
            5,
            CardRarity.R));

    /**
     * Flamme
     */
    public static final Supplier<Card> FLAMME_CARD = INSTANCE.register(() -> new Card("10", "Flamme",
            "https://cdn.myanimelist.net/images/characters/10/528177.jpg",
            20,
            10,
            5,
            CardRarity.SSR));


    public final Set<Card> CARDS = new HashSet<>();
    public final Map<String, String> IMAGE_REFERENCES;

    CardRegistry() {
        IMAGE_REFERENCES = new HashMap<>();
    }

    public Supplier<Card> register(Supplier<Card> cardSupplier) {
        var card = cardSupplier.get();
        CARDS.add(card);
        IMAGE_REFERENCES.put(card.uid(), card.pictureId());
        return cardSupplier;
    }

    public Optional<Card> fromUID(String uid) {
        for (Card card : CARDS) {
            if (card.uid().equals(uid))
                return Optional.of(card);
        }
        return Optional.empty();
    }

    public <T> Optional<T> fromUID(String uid, Function<Card, T> function) {
        throw new RuntimeException("Not implemented yet");
    }

}
