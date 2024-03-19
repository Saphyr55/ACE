package org.utours.ace.profile.model;

public record ClassCoin(Type type) {

    public enum Type {
        EMPTY,
        PRIEST,
        PALADIN,
        SHAMAN,
        MAGE,
        WARRIOR
    }

}
