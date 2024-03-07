package org.utours.ace.infra.profile.firebase;

import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;

public class FirebaseCardMapper {

    Card map(FirebaseCard card) {
        return Card.Builder.newBuilder()
                .withId(card.getId())
                .withName(card.getName())
                .withPictureId(card.getPictureId())
                .withLifePoint(card.getLifePoint())
                .withManaPoint(card.getManaPoint())
                .withAttackPoint(card.getAttackPoint())
                .withRarity(CardRarity.valueOf(card.getRarity()))
                .build();
    }

    FirebaseCard map(Card card) {
        FirebaseCard firebaseCard = new FirebaseCard();
        firebaseCard.setId(card.id());
        firebaseCard.setAttackPoint(card.attackPoint());
        firebaseCard.setLifePoint(card.lifePoint());
        firebaseCard.setManaPoint(card.manaPoint());
        firebaseCard.setPictureId(card.pictureId());
        firebaseCard.setName(card.name());
        firebaseCard.setRarity(card.rarity().name());
        return firebaseCard;
    }


}
