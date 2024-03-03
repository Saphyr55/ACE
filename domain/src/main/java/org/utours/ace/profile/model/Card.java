package org.utours.ace.profile.model;

public record Card(
        String id,
        String name,
        String pictureAddress,
        Integer lifePoint,
        Integer attackPoint,
        Integer manaPoint,
        CardRarity rarity
) {

}
