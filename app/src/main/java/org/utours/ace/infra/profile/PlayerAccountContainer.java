package org.utours.ace.infra.profile;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.infra.profile.firebase.FirebaseDatabaseReferences;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountMapper;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountRepository;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.Optional;

public enum PlayerAccountContainer {
    INSTANCE;

    private final PlayerAccountRepository playerAccountRepository;

    private PlayerAccount currentPlayerAccount;

    PlayerAccountContainer() {
        var database = FirebaseDatabase.getInstance();
        var refs = new FirebaseDatabaseReferences(database);
        this.playerAccountRepository = new FirebasePlayerAccountRepository(
                refs.playerAccountsReference(),
                new FirebasePlayerAccountMapper());
    }

    public PlayerAccountRepository getPlayerAccountRepository() {
        return playerAccountRepository;
    }

    public Optional<PlayerAccount> getCurrentPlayerAccount() {
        return Optional.ofNullable(currentPlayerAccount);
    }

    public void setCurrentPlayerAccount(PlayerAccount currentPlayerAccount) {
        this.currentPlayerAccount = currentPlayerAccount;
    }


}
