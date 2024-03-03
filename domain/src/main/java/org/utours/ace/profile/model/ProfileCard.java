package org.utours.ace.profile.model;

import java.util.function.Supplier;

public record ProfileCard(
        String name,
        String pictureAddress,
        Integer lifePoint,
        Integer attackPoint,
        Integer manaPoint,
        ProfileCardRarity rarity
) {

    public final static Supplier<ProfileCard> ubel = () ->
            new ProfileCard(
                    "Übel",
                    "206725",
                    20,
                    10,
                    5,
                    ProfileCardRarity.UR);


}
