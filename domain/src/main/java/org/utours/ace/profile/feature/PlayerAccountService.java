package org.utours.ace.profile.feature;

import org.utours.ace.common.Quantity;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.StarDust;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DomainService
public final class PlayerAccountService {

    private final CardService cardService;
    private final PlayerAccountRepository playerAccountRepository;
    private PlayerAccount currentPlayerAccount;

    public PlayerAccountService(CardService cardService, PlayerAccountRepository playerAccountRepository) {
        this.cardService = cardService;
        this.playerAccountRepository = playerAccountRepository;
    }

    public PlayerAccount currentPlayerAccount() {
        return currentPlayerAccount;
    }

    public void setCurrentPlayerAccount(Function<PlayerAccount, PlayerAccount> f) {
        currentPlayerAccount = f.apply(currentPlayerAccount);
    }

    public void updateCurrentPlayerAccount() {
        playerAccountRepository.store(currentPlayerAccount);
    }

    public void addCardsInCurrentPlayerAccount(List<Card> cards) {

        PlayerAccount newPlayerAccount = currentPlayerAccount;

        for (Card card : cards) {
            if (currentPlayerAccount.cards().contains(card)) {
                Quantity<StarDust> starDustQuantity = cardService.determineStarDustConversion(card);
                Quantity<StarDust> newStarDustQuantity = newPlayerAccount
                        .starDustQuantity()
                        .add(starDustQuantity);
                newPlayerAccount = PlayerAccount.Builder.of(newPlayerAccount)
                        .withStarDustQuantity(newStarDustQuantity)
                        .build();
            } else {
                Stream<Card> newPlayerAccountCardStream = newPlayerAccount.cards().stream();
                Stream<Card> singletonCard = Set.of(card).stream();
                Set<Card> newCards = Stream.concat(newPlayerAccountCardStream, singletonCard)
                        .collect(Collectors.toSet());
                newPlayerAccount = PlayerAccount.Builder.of(newPlayerAccount)
                        .withCards(Set.copyOf(newCards))
                        .build();
            }
        }

        currentPlayerAccount = newPlayerAccount;
    }


}
