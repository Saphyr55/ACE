package org.utours.ace.game;

import android.view.View;

import org.utours.ace.game.model.GameParty;

public interface OnPressedViewBoard {

    GameParty onPressed(GameParty gameParty, View selectedView, View view);

}
