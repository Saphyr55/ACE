package org.utours.ace.infra.profile.firebase;

import org.utours.ace.common.Quantity;
import org.utours.ace.profile.feature.CardRegistry;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FirebasePlayerAccountMapper {


   PlayerAccount map(FirebasePlayerAccount playerAccount) {
        if (playerAccount == null) return null;
        return new PlayerAccount(
                playerAccount.getUid(),
                playerAccount.getUsername(),
                playerAccount.getEmail(),
                playerAccount.getLevel(),
                playerAccount.getDescription(),
                new Quantity<>(playerAccount.getStarDustQuantity()),
                new Deck(Set.copyOf(cardsOfIds(playerAccount.getDeck()))),
                Set.copyOf(cardsOfIds(playerAccount.getCards()))
        );
    }

    FirebasePlayerAccount map(PlayerAccount playerAccount) {

        if (playerAccount == null) return null;

        var firebasePlayerAccount = new FirebasePlayerAccount();

        firebasePlayerAccount.setUid(playerAccount.uid());
        firebasePlayerAccount.setUsername(playerAccount.username());
        firebasePlayerAccount.setEmail(playerAccount.email());
        firebasePlayerAccount.setDescription(playerAccount.description());
        firebasePlayerAccount.setStarDustQuantity(playerAccount.starDustQuantity().quantity());
        firebasePlayerAccount.setLevel(playerAccount.level());
        firebasePlayerAccount.setCards(playerAccount
                .cards().stream()
                .map(Card::pictureId)
                .collect(Collectors.toList()));
        firebasePlayerAccount.setDeck(playerAccount.deck()
                .cards().stream()
                .map(Card::pictureId)
                .collect(Collectors.toList()));

        return firebasePlayerAccount;
    }

    public List<Card> cardsOfIds(List<String> ids) {
        List<Card> cards = new ArrayList<>();
        List<String> idsList = List.copyOf(ids);
        for (int i = 0; i < ids.size(); i++) {
            for (int j = 0; j < CardRegistry.INSTANCE.CARDS.size(); j++) {
                var card = CardRegistry.INSTANCE.CARDS.get(j).get();
                if (card.pictureId().equals(idsList.get(i))) {
                    cards.add(card);
                }
            }
        }
        return cards;
    }

}
