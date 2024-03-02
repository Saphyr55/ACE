package org.utours.ace.profile.model;

public record ProfileCard(
        String name,
        String pictureAddress,
        Integer lifePoint,
        Integer attackPoint,
        Integer manaPoint,
        ProfileCardRarity rarity
) {

}
