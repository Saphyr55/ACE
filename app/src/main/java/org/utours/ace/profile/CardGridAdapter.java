package org.utours.ace.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.utours.ace.R;
import org.utours.ace.common.annotation.Adapter;
import org.utours.ace.profile.model.ImageProfileCard;
import org.utours.ace.profile.model.ProfileCard;

import java.util.List;
import java.util.Map;


@Adapter
public class CardGridAdapter extends BaseAdapter {

    private final List<ProfileCard> profileCards;
    private final LayoutInflater layoutInflater;
    private final Map<String, ImageProfileCard> cardImages;


    public CardGridAdapter(Context context,
                           List<ProfileCard> profileCards,
                           Map<String, ImageProfileCard> cardImages) {

        this.profileCards = profileCards;
        this.layoutInflater = LayoutInflater.from(context);
        this.cardImages = cardImages;
    }

    @Override
    public int getCount() {
        return profileCards.size();
    }

    @Override
    public Object getItem(int position) {
        return profileCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var card = (ProfileCard) getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_card, parent, false);
        }
        DeckFragment.setupCardView(convertView, card, cardImages);
        return convertView;
    }

}
