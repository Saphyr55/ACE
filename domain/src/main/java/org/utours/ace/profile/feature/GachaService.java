package org.utours.ace.profile.feature;

import org.utours.ace.common.annotation.DomainService;

@DomainService
public class GachaService {

    private final CardService cardService;

    public GachaService(CardService cardService) {
        this.cardService = cardService;
    }

    public void singlePull() {
        System.out.println(cardService.getRandomCards(1, false).get(0));
    }

    public void multiPull() {
        cardService.getRandomCards(6, false).forEach(System.out::println);
    }



}
