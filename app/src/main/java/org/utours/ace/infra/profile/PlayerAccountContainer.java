package org.utours.ace.infra.profile;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.infra.common.http.HttpImageDownloader;
import org.utours.ace.infra.profile.firebase.FirebaseDatabaseReferences;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountMapper;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountRepository;
import org.utours.ace.infra.profile.mal.MalImageCardDownloader;
import org.utours.ace.infra.profile.sqlite.SQLiteImageInstaller;
import org.utours.ace.infra.profile.sqlite.SQLiteImageProvider;
import org.utours.ace.infra.common.sqlite.SQLiteACEDatabaseWrapper;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PlayerAccountContainer {
    INSTANCE;

    private final Map<Context, SQLiteACEDatabaseWrapper> databaseMap;
    private final PlayerAccountRepository playerAccountRepository;
    private PlayerAccount currentPlayerAccount;


    PlayerAccountContainer() {

        var database = FirebaseDatabase.getInstance();
        var refs = new FirebaseDatabaseReferences(database);

        this.databaseMap = new HashMap<>();
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

    public CardService getCardService(Context context) {

        if (!databaseMap.containsKey(context)) {
            databaseMap.put(context, new SQLiteACEDatabaseWrapper(context));
        }

        SQLiteACEDatabaseWrapper ace = databaseMap.get(context);
        assert ace != null;

        return new CardService(
                new SQLiteImageInstaller(ace.getWritableDatabase()),
                new SQLiteImageProvider(ace.getWritableDatabase()),
                new MalImageCardDownloader(new HttpImageDownloader())
        );
    }

}
