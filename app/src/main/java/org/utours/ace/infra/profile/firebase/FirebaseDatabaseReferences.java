package org.utours.ace.infra.profile.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseReferences {

    public static final String PLAYER_ACCOUNTS_REF = "player_accounts";

    private final FirebaseDatabase database;

    public FirebaseDatabaseReferences(FirebaseDatabase database) {
        this.database = database;
    }

    public DatabaseReference playerAccountsReference() {
        return database.getReference(PLAYER_ACCOUNTS_REF);
    }


}
