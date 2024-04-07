package org.utours.ace.profile.model;

public record ClassCoin(Type type) {

    public boolean isEmptyType() {
        return type.equals(Type.EMPTY);
    }

    public static ClassCoin empty() {
        return new ClassCoin(Type.EMPTY);
    }

    public enum Type {
        EMPTY,
        PRIEST,
        PALADIN,
        SHAMAN,
        MAGE,
        WARRIOR
    }

}
