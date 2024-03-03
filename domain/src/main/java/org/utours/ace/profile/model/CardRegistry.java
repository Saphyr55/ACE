package org.utours.ace.profile.model;

import java.util.function.Supplier;

public final class CardRegistry {


    public final static Supplier<Card> UBEL_CARD = () -> new Card(
            "Übel",
            "https://cdn.myanimelist.net/images/characters/2/535522.jpg",
            20,
            10,
            5,
            CardRarity.UR);



}
