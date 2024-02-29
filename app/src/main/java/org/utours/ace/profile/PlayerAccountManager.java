package org.utours.ace.profile;

import com.google.firebase.database.FirebaseDatabase;

import org.utours.ace.profile.firebase.FirebasePlayerAccountMapper;
import org.utours.ace.profile.firebase.FirebaseDatabaseReferences;
import org.utours.ace.profile.firebase.FirebasePlayerAccountRepository;
import org.utours.ace.profile.port.PlayerAccountInventory;

public enum PlayerAccountManager {
    INSTANCE;

    private final PlayerAccountInventory playerAccountInventory;


    PlayerAccountManager() {
        var database = FirebaseDatabase.getInstance();
        var refs = new FirebaseDatabaseReferences(database);
        this.playerAccountInventory = new FirebasePlayerAccountRepository(
                        refs.playerAccountsReference(),
                        new FirebasePlayerAccountMapper());
    }

    public PlayerAccountInventory getPlayerAccountInventory() {
        return playerAccountInventory;
    }

}
