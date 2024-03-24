package org.utours.ace.infra.game;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.game.port.GamePartyFinder;
import org.utours.ace.infra.common.firebase.FirebaseDatabaseReferences;
import org.utours.ace.infra.game.firebase.FirebaseGamePartyFinder;

public enum GameContainer {
    INSTANCE;


    private final GamePartyFinder gamePartyFinder;

    GameContainer() {
        var database = FirebaseDatabase.getInstance();
        var refs = new FirebaseDatabaseReferences(database);

        gamePartyFinder = new FirebaseGamePartyFinder(refs.gamePartiesReference());

    }

    public GamePartyFinder getGamePartyFinder() {
        return gamePartyFinder;
    }

}
