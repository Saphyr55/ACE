package org.utours.ace.game.use_case;

import android.view.View;

import org.utours.ace.game.OnPressViewBoard;
import org.utours.ace.game.model.GameParty;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.Map;
import java.util.function.Function;

public class PlayerOnPressCardViewBoard implements OnPressViewBoard {

    private final Map<View, Card> playerCardViews;
    private final Map<View, Card> opponentCardView;
    private final Function<Card, ImageCard> getImageCardByCard;

    public PlayerOnPressCardViewBoard(Map<View, Card> playerCardViews,
                                      Map<View, Card> opponentCardView,
                                      Function<Card, ImageCard> getImageCardByCard) {

        this.playerCardViews = playerCardViews;
        this.opponentCardView = opponentCardView;
        this.getImageCardByCard = getImageCardByCard;
    }

    @Override
    public GameParty onPress(GameParty gameParty, View selectedView, View view) {

        // View gameCardBoardView = view.findViewById(R.id.view_gameBoardCard);

        // startRotation(selectedView, 20);

        return gameParty;
    }


}
