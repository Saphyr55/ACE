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
                    "206725",
                    20,
                    10,
                    5,
                    CardRarity.UR);


}
