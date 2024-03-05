package org.utours.ace.profile.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DomainService
public class GachaService {

    public static final int MULTI_QUANTITY = 10;
    public static final int SINGLE_QUANTITY = 1;

    private final CardService cardService;

    public GachaService(CardService cardService) {
        this.cardService = cardService;
    }

    public Card singlePull() {
        return cardService.getRandomCards(SINGLE_QUANTITY, false).get(0);
    }

    public List<Card> multiPull() {
        return List.copyOf(cardService.getRandomCards(MULTI_QUANTITY, false));
    }

    public List<Card> pullCards(int pullQuantity) {
        List<Card> cards;
        if (pullQuantity == GachaService.MULTI_QUANTITY)
            cards = multiPull();
        else if (pullQuantity == GachaService.SINGLE_QUANTITY) {
            cards = List.of(singlePull());
        } else {
            throw new RuntimeException();
        }
        System.out.println("Quantity=" + pullQuantity + " CARDS: " + cards);
        return cards;
    }


}
