package org.utours.ace.infra.profile.firebase;

import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.CardRarity;
import org.utours.ace.profile.model.ClassCoin;

import java.util.List;
import java.util.stream.Collectors;

public class FirebaseCardMapper {

    public Card map(FirebaseCard card) {
        if (card.getEmpty()) return null;
        return Card.Builder.newBuilder()
                .withId(card.getId())
                .withName(card.getName())
                .withPictureId(card.getPictureId())
                .withLifePoint(card.getLifePoint())
                .withManaPoint(card.getManaPoint())
                .withAttackPoint(card.getAttackPoint())
                .withClassCoin(new ClassCoin(ClassCoin.Type.valueOf(card.getClassCoin())))
                .withRarity(CardRarity.valueOf(card.getRarity()))
                .build();
    }

    public FirebaseCard map(Card card) {
        FirebaseCard firebaseCard = new FirebaseCard();
        if (card == null) {
            firebaseCard.setEmpty(true);
            return firebaseCard;
        }
        firebaseCard.setId(card.uid());
        firebaseCard.setAttackPoint(card.attackPoint());
        firebaseCard.setLifePoint(card.lifePoint());
        firebaseCard.setManaPoint(card.manaPoint());
        firebaseCard.setPictureId(card.pictureId());
        firebaseCard.setName(card.name());
        firebaseCard.setRarity(card.rarity().name());
        firebaseCard.setClassCoin(card.classCoin().type().name());
        return firebaseCard;
    }

    public List<Card> mapFs(List<FirebaseCard> cards) {
        return cards.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<FirebaseCard> map(List<Card> cards) {
        return cards.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
