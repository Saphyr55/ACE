package org.utours.ace.profile.port;

import org.utours.ace.profile.model.ClassCoin;

public interface ClassCoinInformation {

    String description(ClassCoin.Type type);

    String name(ClassCoin.Type type);

}
