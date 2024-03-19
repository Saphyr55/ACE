package org.utours.ace.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.cardview.widget.CardView;

import org.utours.ace.R;
import org.utours.ace.common.annotation.Adapter;
import org.utours.ace.profile.deck.DeckFragment;
import org.utours.ace.profile.model.ImageCard;
import org.utours.ace.profile.model.Card;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


@Adapter
public class CardGridAdapter extends BaseAdapter {

    private List<Card> cards;
    private final LayoutInflater layoutInflater;
    private final Map<String, ImageCard> cardImages;
    private BiConsumer<CardView, Card> onCreateCardView;

    public CardGridAdapter(Context context,
                           List<Card> cards,
                           Map<String, ImageCard> cardImages,
                           BiConsumer<CardView, Card> onCreateCardView) {

        this.cards = cards;
        this.layoutInflater = LayoutInflater.from(context);
        this.cardImages = cardImages;
        this.onCreateCardView = onCreateCardView;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        var card = (Card) getItem(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_card, parent, false);
        }

        DeckFragment.bindCardView(convertView, card, onCreateCardView, cardImages);

        return convertView;
    }

    public void setOnCreateCardView(BiConsumer<CardView, Card> onCreateCardView) {
        this.onCreateCardView = onCreateCardView;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
