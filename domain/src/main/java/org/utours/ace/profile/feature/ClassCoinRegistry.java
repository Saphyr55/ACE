package org.utours.ace.profile.feature;

import org.utours.ace.profile.model.ClassCoin;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public enum ClassCoinRegistry {
    INSTANCE;

    {
        register(() -> new ClassCoin(ClassCoin.Type.PRIEST));
        register(() -> new ClassCoin(ClassCoin.Type.PALADIN));
        register(() -> new ClassCoin(ClassCoin.Type.SHAMAN));
        register(() -> new ClassCoin(ClassCoin.Type.MAGE));
        register(() -> new ClassCoin(ClassCoin.Type.WARRIOR));
    }

    public final Set<ClassCoin> classCoins = new HashSet<>();

    private void register(Supplier<ClassCoin> classCoinSupplier) {
        classCoins.add(classCoinSupplier.get());
    }

    public static ClassCoin getClassCoin(ClassCoin.Type type) {
        return INSTANCE.classCoins.stream()
                .filter(classCoin -> classCoin.type().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

}
