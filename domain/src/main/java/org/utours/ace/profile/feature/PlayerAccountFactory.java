package org.utours.ace.profile.feature;

import org.utours.ace.common.Quantity;
import org.utours.ace.profile.model.Deck;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.model.StarDust;

import java.util.List;
import java.util.Set;

public interface PlayerAccountFactory {

    PlayerAccountFactory DEFAULT = (uid, email) ->
            new PlayerAccount(uid,
                    "Player",
                    email,
                    1,
                    "",
                    new Quantity<>(3000),
                    new Deck(Set.of(
                            CardRegistry.UBEL_CARD.get(),
                            CardRegistry.HIMMEL_CARD.get(),
                            CardRegistry.FERN_CARD.get()
                    )),
                    Set.of(
                            CardRegistry.UBEL_CARD.get(),
                            CardRegistry.EISEN_CARD.get(),
                            CardRegistry.FRIEREN_CARD.get(),
                            CardRegistry.STARK_CARD.get(),
                            CardRegistry.HIMMEL_CARD.get(),
                            CardRegistry.FERN_CARD.get()
                        ));

    PlayerAccount create(String uid, String email);

}
