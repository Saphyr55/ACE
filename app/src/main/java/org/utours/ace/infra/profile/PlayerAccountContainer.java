package org.utours.ace.infra.profile;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.infra.profile.firebase.FirebaseDatabaseReferences;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountMapper;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountRepository;
import org.utours.ace.infra.profile.sqlite.SQLiteImageProfileInsertion;
import org.utours.ace.infra.profile.sqlite.SQLiteImageProfileProvider;
import org.utours.ace.infra.sqlite.SQLiteACEDatabase;
import org.utours.ace.profile.feature.PlayerAccountService;
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
        this.playerAccountRepository =
                new FirebasePlayerAccountRepository(
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

    public PlayerAccountService getPlayerAccountService(Context context) {
        try (var aceDB = new SQLiteACEDatabase(context)) {
            return new PlayerAccountService(
                    new SQLiteImageProfileInsertion(aceDB.getWritableDatabase()),
                    new SQLiteImageProfileProvider(aceDB.getWritableDatabase()),
                    playerAccountRepository
            );
        }
    }

}
