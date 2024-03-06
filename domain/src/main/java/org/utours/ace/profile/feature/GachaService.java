package org.utours.ace.profile.feature;

import org.utours.ace.common.Quantity;
import org.utours.ace.common.Result;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.exception.NotEnoughTicketException;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.Ticket;

import java.util.List;

@DomainService
public class GachaService {

    public static final int MULTI_QUANTITY = 10;
    public static final int SINGLE_QUANTITY = 1;

    private final CardService cardService;
    private final PlayerAccountService playerAccountService;

    public GachaService(CardService cardService, PlayerAccountService playerAccountService) {
        this.cardService = cardService;
        this.playerAccountService = playerAccountService;
    }

    public Result<List<Card>, NotEnoughTicketException> pullCards(int pullQuantity) {

        PlayerAccount currentPlayerAccount = playerAccountService.currentPlayerAccount();
        Quantity<Ticket> cTicketQuantity = currentPlayerAccount.ticketQuantity();

        if (cTicketQuantity.quantity() < pullQuantity) {
            return Result.errorOf(new NotEnoughTicketException());
        }

        var cards = cardService.probabilistStochasticCards(pullQuantity);
        playerAccountService.setCurrentPlayerAccount(playerAccount -> {
            Quantity<Ticket> ticketQuantity = playerAccount.ticketQuantity().sub(pullQuantity);
            return playerAccount.ticketQuantity(ticketQuantity);
        });

        playerAccountService.addCardsInCurrentPlayerAccount(cards);
        playerAccountService.updateCurrentPlayerAccount();

        return Result.successOf(cards);
    }

}
