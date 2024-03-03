package org.utours.ace.infra.profile.firebase;

import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ProfileDeck;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

                // TODO: Remove this debug thing.
                IntStream.range(0, 30)
                        .mapToObj(value -> Card.ubel.get())
                        .collect(Collectors.toList()),

                // TODO: Remove this debug thing.
                new ProfileDeck(
                        Card.ubel.get(),
                        Card.ubel.get(),
                        Card.ubel.get(),
                        Card.ubel.get(),
                        Card.ubel.get()));
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
