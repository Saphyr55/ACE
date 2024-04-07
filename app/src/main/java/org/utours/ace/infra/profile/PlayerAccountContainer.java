package org.utours.ace.infra.profile;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.infra.common.http.HttpImageDownloader;
import org.utours.ace.infra.profile.firebase.FirebaseCardMapper;
import org.utours.ace.infra.common.firebase.FirebaseDatabaseReferences;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountMapper;
import org.utours.ace.infra.profile.firebase.FirebasePlayerAccountRepository;
import org.utours.ace.infra.profile.jikan.JikanInformationCardProvider;
import org.utours.ace.infra.profile.mal.MalImageCardDownloader;
import org.utours.ace.infra.profile.sqlite.SQLiteImageInstaller;
import org.utours.ace.infra.profile.sqlite.SQLiteImageProvider;
import org.utours.ace.infra.common.sqlite.SQLiteACEDatabaseWrapper;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.GachaService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.port.ImageCardDownloader;
import org.utours.ace.profile.port.InformationCardProvider;
import org.utours.ace.profile.port.PlayerAccountRepository;

public enum PlayerAccountContainer {
    INSTANCE;

    private SQLiteACEDatabaseWrapper sqLiteACEDatabaseWrapper;
    private final PlayerAccountRepository playerAccountRepository;
    private final PlayerAccountService playerAccountService;
    private final ImageCardDownloader imageCardDownloader;
    private final InformationCardProvider informationCardProvider;

    PlayerAccountContainer() {

        var database = FirebaseDatabase.getInstance();
        var refs = new FirebaseDatabaseReferences(database);

        var cardMapper = new FirebaseCardMapper();
        var playerAccountMapper = new FirebasePlayerAccountMapper(cardMapper);

        this.playerAccountRepository = new FirebasePlayerAccountRepository(refs.playerAccountsReference(), playerAccountMapper);
        this.imageCardDownloader = new MalImageCardDownloader(new HttpImageDownloader());
        this.playerAccountService = new PlayerAccountService(playerAccountRepository);
        this.informationCardProvider = new JikanInformationCardProvider();
    }

    public InformationCardProvider getInformationCardProvider() {
        return informationCardProvider;
    }

    public PlayerAccountRepository getPlayerAccountRepository() {
        return playerAccountRepository;
    }

    public PlayerAccountService getService() {
        return playerAccountService;
    }

    public CardService getCardService(Context context) {

        if (sqLiteACEDatabaseWrapper == null) {
            sqLiteACEDatabaseWrapper = new SQLiteACEDatabaseWrapper(context);
        }

        SQLiteACEDatabaseWrapper ace = sqLiteACEDatabaseWrapper;

        return new CardService(
                new SQLiteImageInstaller(ace.getWritableDatabase()),
                new SQLiteImageProvider(ace.getWritableDatabase()),
                imageCardDownloader
        );
    }

    public GachaService getGachaService(Context context) {
        return getGachaService(getCardService(context));
    }

    public GachaService getGachaService(CardService cardService) {
        return new GachaService(cardService, playerAccountService);
    }

}
