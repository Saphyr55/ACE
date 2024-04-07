package org.utours.ace.profile.deck;


import android.view.View;

import org.utours.ace.profile.model.ClassCoin;

@FunctionalInterface
public interface OnClickCassCoin {

    void onClick(ClassCoin classCoin, View itemView);

}
