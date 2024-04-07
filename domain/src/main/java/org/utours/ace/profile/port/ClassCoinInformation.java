package org.utours.ace.profile.port;

import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.model.ClassCoinImage;

public interface ClassCoinInformation<T> {

    String description(ClassCoin.Type type);

    String information(ClassCoin.Type type);

    T image(ClassCoin.Type type);

}
