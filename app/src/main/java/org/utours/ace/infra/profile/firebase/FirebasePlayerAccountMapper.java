package org.utours.ace.infra.profile.firebase;

import org.utours.ace.common.Quantity;
import org.utours.ace.profile.feature.CardRegistry;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.StarDust;
import org.utours.ace.profile.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FirebasePlayerAccountMapper {

    private final FirebaseCardMapper cardMapper;

    public FirebasePlayerAccountMapper(FirebaseCardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    PlayerAccount map(FirebasePlayerAccount account) {

        if (account == null) return null;

        Set<Card> cards = account.getCards().stream()
                .map(cardMapper::map)
                .collect(Collectors.toSet());

        List<ClassCoin> classCoins = account.getClassCoins().stream()
                .map(s -> new ClassCoin(ClassCoin.Type.valueOf(s)))
                .collect(Collectors.toList());

        Deck deck = new Deck(Set.copyOf(cardsOfIds(account.getDeck())));
        Quantity<StarDust> starDustQuantity = new Quantity<>(account.getStarDustQuantity());
        Quantity<Ticket> ticketQuantity = new Quantity<>(account.getTicketQuantity());

        return PlayerAccount.Builder
                .copyOf(account.getUid(), account.getEmail())
                .withDeck(deck)
                .withLevel(account.getLevel())
                .withUsername(account.getUsername())
                .withDescription(account.getDescription())
                .withStarDustQuantity(starDustQuantity)
                .withTicketQuantity(ticketQuantity)
                .withCards(cards)
                .withClassCoins(classCoins)
                .build();
    }

    FirebasePlayerAccount map(PlayerAccount playerAccount) {

        if (playerAccount == null) return null;

        var firebasePlayerAccount = new FirebasePlayerAccount();

        firebasePlayerAccount.setUid(playerAccount.uid());
        firebasePlayerAccount.setUsername(playerAccount.username());
        firebasePlayerAccount.setEmail(playerAccount.email());
        firebasePlayerAccount.setDescription(playerAccount.description());
        firebasePlayerAccount.setStarDustQuantity(playerAccount.starDustQuantity().quantity());
        firebasePlayerAccount.setTicketQuantity(playerAccount.ticketQuantity().quantity());
        firebasePlayerAccount.setLevel(playerAccount.level());

        firebasePlayerAccount.setClassCoins(playerAccount
                .cards().stream()
                .map(card -> card.classCoin().type().name())
                .collect(Collectors.toList()));

        firebasePlayerAccount.setCards(playerAccount
                .cards().stream()
                .map(cardMapper::map)
                .collect(Collectors.toList()));

        firebasePlayerAccount.setDeck(playerAccount.deck()
                .cards().stream()
                .map(Card::uid)
                .collect(Collectors.toList()));

        return firebasePlayerAccount;
    }

    public List<Card> cardsOfIds(List<String> ids) {

        var cards = new ArrayList<Card>();
        var idsList = List.copyOf(ids);
        var allCard = List.copyOf(CardRegistry.INSTANCE.CARDS);

        for (int i = 0; i < ids.size(); i++) {
            for (int j = 0; j < allCard.size(); j++) {
                var card = allCard.get(j);
                if (card.uid().equals(idsList.get(i))) {
                    cards.add(card);
                }
            }
        }

        return cards;
    }

}
