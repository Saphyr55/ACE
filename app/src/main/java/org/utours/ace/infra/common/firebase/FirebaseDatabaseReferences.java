package org.utours.ace.infra.common.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseReferences {

    public static final String PLAYER_ACCOUNTS_REF = "player_accounts";
    public static final String GAME_PARTIES_REF = "game_parties";
    public static final String GAME_PARTIES_WAITING_REF = "waiting";
    private final FirebaseDatabase database;

    public FirebaseDatabaseReferences(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference playerAccountsReference() {
        return database.getReference(PLAYER_ACCOUNTS_REF);
    }

    public DatabaseReference gamePartiesReference() {
        return database.getReference(GAME_PARTIES_REF);
    }


}
