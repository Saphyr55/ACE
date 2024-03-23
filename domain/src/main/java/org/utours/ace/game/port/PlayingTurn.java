package org.utours.ace.game.port;

import org.utours.ace.game.model.GameParty;
import org.utours.ace.profile.model.Card;

public interface PlayingTurn {


    void onTurn(GameParty gameParty, Card card);


}
