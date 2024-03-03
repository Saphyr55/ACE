package org.utours.ace.profile.feature;

import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.ProfileCard;
import org.utours.ace.profile.model.ProfileDeck;

import java.util.List;

public interface PlayerAccountFactory {

    PlayerAccountFactory DEFAULT = (uid, email) ->
            new PlayerAccount(uid, "Player", email, 1, "",
                    // TODO: Remove this debug things.
                    List.of(),
                    new ProfileDeck(
                    ProfileCard.ubel.get(),
                    ProfileCard.ubel.get(),
                    ProfileCard.ubel.get(),
                    ProfileCard.ubel.get(),
                    ProfileCard.ubel.get()));

    PlayerAccount create(String uid, String email);


}
