package org.utours.ace.profile.feature;

import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.function.Function;

@DomainService
public final class PlayerAccountService {

    private final PlayerAccountRepository playerAccountRepository;
    private PlayerAccount currentPlayerAccount;

    public PlayerAccountService(PlayerAccountRepository playerAccountRepository) {

        this.playerAccountRepository = playerAccountRepository;
    }

    public PlayerAccount currentPlayerAccount() {
        return currentPlayerAccount;
    }

    public void updateCurrentPlayerAccount(Function<PlayerAccount, PlayerAccount> f) {
        currentPlayerAccount = f.apply(currentPlayerAccount);
    }

    public void storeCurrentPlayerAccount() {
        playerAccountRepository.store(currentPlayerAccount);
    }


}
