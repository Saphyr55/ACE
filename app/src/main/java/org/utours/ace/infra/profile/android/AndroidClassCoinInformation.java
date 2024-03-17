package org.utours.ace.infra.profile.android;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.loader.ResourcesProvider;

import androidx.core.util.Consumer;

import org.utours.ace.R;
import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.port.ClassCoinInformation;

public class AndroidClassCoinInformation implements ClassCoinInformation {

    private final Resources resources;

    public AndroidClassCoinInformation(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String description(ClassCoin.Type type) {
        return switch (type) {
            case EMPTY -> resources.getString(R.string.unknown);
            case PRIEST -> resources.getString(R.string.unknown);
            case PALADIN -> resources.getString(R.string.unknown);
            case SHAMAN -> resources.getString(R.string.unknown);
            case MAGE -> resources.getString(R.string.unknown);
            case WARRIOR -> resources.getString(R.string.unknown);
        };
    }

    @Override
    public String name(ClassCoin.Type type) {
        return switch (type) {
            case EMPTY -> resources.getString(R.string.unknown);
            case PRIEST -> resources.getString(R.string.unknown);
            case PALADIN -> resources.getString(R.string.unknown);
            case SHAMAN -> resources.getString(R.string.unknown);
            case MAGE -> resources.getString(R.string.unknown);
            case WARRIOR -> resources.getString(R.string.unknown);
        };
    }

}
