package org.utours.ace.profile.feature;

import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public enum CardRegistry {
    INSTANCE;

    public final static Supplier<Card> FERN_CARD = INSTANCE.register(() -> new Card("7", "Fern",
            "https://cdn.myanimelist.net/images/characters/13/519083.jpg",
            20,
            10,
            5,
            CardRarity.SSR));

    public final static Supplier<Card> UBEL_CARD = INSTANCE.register(() -> new Card("6", "Übel",
            "https://cdn.myanimelist.net/images/characters/2/535522.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    public final static Supplier<Card> FRIEREN_CARD = INSTANCE.register(() -> new Card("5", "Frieren",
            "https://cdn.myanimelist.net/images/characters/4/420348.jpg",
            20,
            10,
            5,
            CardRarity.UR));

    public final static Supplier<Card> STARK_CARD = INSTANCE.register(() -> new Card("4", "Stark",
            "https://cdn.myanimelist.net/images/characters/3/423034.jpg",
            20,
            10,
            5,
            CardRarity.R));

    public final static Supplier<Card> HIMMEL_CARD = INSTANCE.register(() -> new Card("3", "Himmel",
            "https://cdn.myanimelist.net/images/characters/6/471527.jpg",
            20,
            10,
            5,
            CardRarity.SSR));

    public final static Supplier<Card> EISEN_CARD = INSTANCE.register(() -> new Card("2", "Eisen",
            "https://cdn.myanimelist.net/images/characters/12/471526.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    public final static Supplier<Card> HEITER_CARD = INSTANCE.register(() -> new Card("1", "Heiter",
            "https://cdn.myanimelist.net/images/characters/9/471528.jpg",
            20,
            10,
            5,
            CardRarity.SR));

    public final static Supplier<Card> METHODE_CARD = INSTANCE.register(() -> new Card("8", "Methode",
            "https://cdn.myanimelist.net/images/characters/14/538692.jpg",
            20,
            10,
            5,
            CardRarity.R));

    public final static Supplier<Card> SEIN_CARD = INSTANCE.register(() -> new Card("9", "Sein",
            "https://cdn.myanimelist.net/images/characters/3/442553.jpg",
            20,
            10,
            5,
            CardRarity.R));

    public final static Supplier<Card> FLAMME_CARD = INSTANCE.register(() -> new Card("10", "Flamme",
            "https://cdn.myanimelist.net/images/characters/10/528177.jpg",
            20,
            10,
            5,
            CardRarity.R));

    public final List<Supplier<Card>> CARDS = new LinkedList<>();
    public final Map<String, String> REFS;

    CardRegistry() {
        REFS = new HashMap<>();
    }

    public Supplier<Card> register(Supplier<Card> cardSupplier) {
        CARDS.add(cardSupplier);
        return cardSupplier;
    }


}
