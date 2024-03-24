package org.utours.ace.infra.game;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.game.port.GamePartyFinder;
import org.utours.ace.game.port.GamePartyMapper;
import org.utours.ace.infra.common.firebase.*;
import org.utours.ace.infra.game.firebase.FirebaseBackendGamePartyMapper;
import org.utours.ace.infra.game.firebase.FirebaseGamePartyFinder;
import org.utours.ace.infra.profile.PlayerAccountContainer;

public enum GameContainer {
    INSTANCE;


    private final FirebaseDatabaseReferences refs;
    private GamePartyFinder gamePartyFinder;

    GameContainer() {
        var database = FirebaseDatabase.getInstance();
        refs = new FirebaseDatabaseReferences(database);
    }

    public GamePartyFinder getGamePartyFinder(Context context) {
        if (gamePartyFinder == null) {
            gamePartyFinder = new FirebaseGamePartyFinder(
                    PlayerAccountContainer.INSTANCE.getPlayerAccountRepository(),
                    PlayerAccountContainer.INSTANCE.getCardService(context),
                    refs.gamePartiesReference(),
                    refs.waitingGamePartiesReference()
            );
        }
        return gamePartyFinder;
    }

    public GamePartyMapper getBackendGamePartyMapper() {
        return new FirebaseBackendGamePartyMapper(refs.gamePartiesReference());
    }

}
