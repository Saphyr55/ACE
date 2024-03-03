package org.utours.ace.profile.feature;

import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;

import java.util.List;

public interface PlayerAccountFactory {

    PlayerAccountFactory DEFAULT = (uid, email) ->
            new PlayerAccount(uid, "Player", email, 1, "",
                    new Deck(List.of(
                            CardRegistry.UBEL_CARD.get(),
                            CardRegistry.HIMMEL_CARD.get(),
                            CardRegistry.FERN_CARD.get()
                    )),
                    List.of(
                            CardRegistry.UBEL_CARD.get(),
                            CardRegistry.EISEN_CARD.get(),
                            CardRegistry.FRIEREN_CARD.get(),
                            CardRegistry.STARK_CARD.get(),
                            CardRegistry.HIMMEL_CARD.get(),
                            CardRegistry.FERN_CARD.get()
                        ));

    PlayerAccount create(String uid, String email);

}
