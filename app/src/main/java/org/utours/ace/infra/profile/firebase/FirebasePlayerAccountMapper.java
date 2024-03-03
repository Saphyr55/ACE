package org.utours.ace.infra.profile.firebase;

import org.utours.ace.profile.feature.CardRegistry;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FirebasePlayerAccountMapper {


    PlayerAccount map(FirebasePlayerAccount playerAccount) {
        if (playerAccount == null) return null;
        return new PlayerAccount(
                playerAccount.getUid(),
                playerAccount.getUsername(),
                playerAccount.getEmail(),
                playerAccount.getLevel(),
                playerAccount.getDescription(),
                new Deck(stringsToCards(playerAccount.getDeck())),
                stringsToCards(playerAccount.getCards())
        );
    }

    FirebasePlayerAccount map(PlayerAccount playerAccount) {

        if (playerAccount == null) return null;

        var firebasePlayerAccount = new FirebasePlayerAccount();

        firebasePlayerAccount.setUid(playerAccount.uid());
        firebasePlayerAccount.setUsername(playerAccount.username());
        firebasePlayerAccount.setEmail(playerAccount.email());
        firebasePlayerAccount.setDescription(playerAccount.description());
        firebasePlayerAccount.setLevel(playerAccount.level());

        firebasePlayerAccount.setCards(playerAccount
                .cards().stream()
                .map(Card::pictureAddress)
                .collect(Collectors.toList()));

        firebasePlayerAccount.setDeck(playerAccount
                .deck().cards().stream()
                .map(Card::pictureAddress)
                .collect(Collectors.toList()));

        return firebasePlayerAccount;
    }

    public List<Card> stringsToCards(List<String> ids) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            for (int j = 0; j < CardRegistry.INSTANCE.CARDS.size(); j++) {
                var card = CardRegistry.INSTANCE.CARDS.get(j).get();
                if (card.pictureAddress().equals(ids.get(i))) {
                    cards.add(card);
                }
            }
        }
        return cards;
    }

}
