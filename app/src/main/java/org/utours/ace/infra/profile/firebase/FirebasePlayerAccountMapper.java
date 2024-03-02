package org.utours.ace.infra.profile.firebase;

import org.utours.ace.profile.model.PlayerAccount;

import java.util.List;

public class FirebasePlayerAccountMapper {

    public FirebasePlayerAccountMapper() {

    }

    PlayerAccount map(FirebasePlayerAccount playerAccount) {

        if (playerAccount == null) return null;

        return new PlayerAccount(
                playerAccount.getUid(),
                playerAccount.getUsername(),
                playerAccount.getEmail(),
                playerAccount.getLevel(),
                playerAccount.getDescription(),
                List.of());
    }

    FirebasePlayerAccount map(PlayerAccount playerAccount) {

        if (playerAccount == null) return null;

        var firebasePlayerAccount = new FirebasePlayerAccount();

        firebasePlayerAccount.setUid(playerAccount.uid());
        firebasePlayerAccount.setUsername(playerAccount.username());
        firebasePlayerAccount.setEmail(playerAccount.email());
        firebasePlayerAccount.setDescription(playerAccount.description());
        firebasePlayerAccount.setLevel(playerAccount.level());

        return firebasePlayerAccount;
    }

}
