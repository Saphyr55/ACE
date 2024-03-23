package org.utours.ace.game;

import android.view.View;

import org.utours.ace.common.Selectable;
import org.utours.ace.game.model.GameParty;

public interface OnPressViewBoard {

    GameParty onPress(GameParty gameParty, View selectedView, View view);

}
