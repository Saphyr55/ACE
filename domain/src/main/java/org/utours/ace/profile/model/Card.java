package org.utours.ace.profile.model;

public record Card(
        String id,
        String name,
        String pictureId,
        Integer lifePoint,
        Integer attackPoint,
        Integer manaPoint,
        CardRarity rarity
) {

}
