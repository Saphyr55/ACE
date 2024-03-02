package org.utours.ace.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.common.annotation.Adapter;
import org.utours.ace.profile.model.ProfileCard;

import java.util.List;


@Adapter
public class CardGridAdapter extends BaseAdapter {

    private final List<ProfileCard> profileCards;
    private final LayoutInflater layoutInflater;


    public CardGridAdapter(Context context,
                           List<ProfileCard> profileCards) {

        this.profileCards = profileCards;
        this.layoutInflater = LayoutInflater.from(context);
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

        ImageView iconImageView = convertView.findViewById(R.id.image_card);
        TextView nameTextView = convertView.findViewById(R.id.text_nameCard);
        TextView attackPointTextView = convertView.findViewById(R.id.text_attackPointCard);
        TextView lifePointTextView = convertView.findViewById(R.id.text_lifePointCard);
        TextView manaPointTextView = convertView.findViewById(R.id.text_manaPointCard);
        TextView rarityTextView = convertView.findViewById(R.id.text_rarity);

        iconImageView.setImageResource(R.drawable.picture_ubel);
        nameTextView.setText(card.name());
        attackPointTextView.setText(String.valueOf(card.attackPoint()));
        lifePointTextView.setText(String.valueOf(card.lifePoint()));
        manaPointTextView.setText(String.valueOf(card.manaPoint()));
        rarityTextView.setText(card.rarity().name());

        return convertView;
    }

}
