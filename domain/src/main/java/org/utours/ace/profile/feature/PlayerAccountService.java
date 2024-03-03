package org.utours.ace.profile.feature;

import org.utours.ace.common.FunctionalUtils;
import org.utours.ace.common.annotation.DomainService;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.ImageCardInstaller;
import org.utours.ace.profile.port.ImageCardProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@DomainService
public final class PlayerAccountService {

    private PlayerAccount currentPlayerAccount;

    public PlayerAccountService() {

    }

    public PlayerAccount currentPlayerAccount() {
        return currentPlayerAccount;
    }

    public void updateCurrentPlayerAccount(PlayerAccount playerAccount) {
        currentPlayerAccount = playerAccount;
    }


}
