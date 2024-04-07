package org.utours.ace.infra.game.firebase;

import com.google.firebase.database.DatabaseReference;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.game.port.GamePartyMapper;

public class FirebaseBackendGamePartyMapper implements GamePartyMapper {

    private final DatabaseReference databaseReference;
    private final FirebaseGamePartyMapper mapper;

    public FirebaseBackendGamePartyMapper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        this.mapper = new FirebaseGamePartyMapper(new FirebasePlayerMapper());

    }

    @Override
    public GameParty map(GameParty gameParty) {
        FirebaseGameParty fgp = mapper.map(gameParty);
        databaseReference.child(gameParty.uid()).setValue(fgp);
        return gameParty;
    }

}
