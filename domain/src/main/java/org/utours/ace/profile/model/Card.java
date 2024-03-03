package org.utours.ace.profile.model;

import java.util.function.Supplier;

public record Card(
        String name,
        String pictureAddress,
        Integer lifePoint,
        Integer attackPoint,
        Integer manaPoint,
        CardRarity rarity
) {

    public final static Supplier<Card> ubel = () ->
            new Card(
                    "Übel",
                    "https://cdn.myanimelist.net/images/characters/2/535522.jpg",
                    20,
                    10,
                    5,
                    CardRarity.UR);


}
