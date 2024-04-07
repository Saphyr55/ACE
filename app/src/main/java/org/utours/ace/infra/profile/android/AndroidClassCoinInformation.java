package org.utours.ace.infra.profile.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.utours.ace.R;
import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.port.ClassCoinInformation;

import java.util.HashMap;
import java.util.Map;

public class AndroidClassCoinInformation implements ClassCoinInformation<Bitmap> {
    
    private final Context context;
    private final Map<ClassCoin.Type, Bitmap> typeClassCoinImages;

    public AndroidClassCoinInformation(Context context) {
        this.context = context;
        typeClassCoinImages = new HashMap<>();
    }

    @Override
    public String description(ClassCoin.Type type) {
        return switch (type) {
            case EMPTY -> context.getString(R.string.unknown);
            case PRIEST -> context.getString(R.string.unknown);
            case PALADIN -> context.getString(R.string.unknown);
            case SHAMAN -> context.getString(R.string.unknown);
            case MAGE -> context.getString(R.string.unknown);
            case WARRIOR -> context.getString(R.string.unknown);
        };
    }

    @Override
    public String information(ClassCoin.Type type) {
        return switch (type) {
            case EMPTY -> context.getString(R.string.unknown);
            case PRIEST -> context.getString(R.string.unknown);
            case PALADIN -> context.getString(R.string.unknown);
            case SHAMAN -> context.getString(R.string.unknown);
            case MAGE -> context.getString(R.string.unknown);
            case WARRIOR -> context.getString(R.string.unknown);
        };
    }

    @Override
    public Bitmap image(ClassCoin.Type type) {
        if (!typeClassCoinImages.containsKey(type)) {
            typeClassCoinImages.put(type, switch (type) {
                case EMPTY -> BitmapFactory.decodeResource(context.getResources(), R.drawable.weapon);
                case PRIEST -> BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_coin_yellow);
                case PALADIN -> BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_coin_blue);
                case SHAMAN -> BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_coin_orange);
                case MAGE -> BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_coin_red);
                case WARRIOR -> BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_coin_black);
            });
        }
        return typeClassCoinImages.get(type);
    }

}
