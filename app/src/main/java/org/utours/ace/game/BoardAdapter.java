package org.utours.ace.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.utours.ace.R;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.Map;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CardViewHolder> {

    private final List<Card> cards;
    private final Map<String, ImageCard> imageCardRefs;

    public BoardAdapter(List<Card> cards, Map<String, ImageCard> imageCardRefs) {
        this.cards = cards;
        this.imageCardRefs = imageCardRefs;
    }

    @NonNull
    @Override
    public BoardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_game_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.CardViewHolder holder, int position) {
        Card card = cards.get(position);
        ImageCard imageCard = imageCardRefs.get(card.pictureId());
        holder.bind(card, imageCard);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        private final CardViewBinding cardViewBinding;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewBinding = new CardViewBinding(itemView);
        }

        public void bind(Card card, ImageCard imageCard) {
            cardViewBinding.bind(card, imageCard);
        }

    }
}
