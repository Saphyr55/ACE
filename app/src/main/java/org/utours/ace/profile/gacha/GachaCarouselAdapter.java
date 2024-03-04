package org.utours.ace.profile.gacha;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import org.utours.ace.R;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.List;
import java.util.Map;

public class GachaCarouselAdapter
        extends RecyclerView.Adapter<GachaCarouselAdapter.CarouselViewHolder> {

    private final List<Card> imageCards;
    private final Map<String, ImageCard> refs;

    public GachaCarouselAdapter(List<Card> imageCards,
                                Map<String, ImageCard> refs) {
        this.imageCards = imageCards;
        this.refs = refs;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_carousel_card_gacha, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        var card = imageCards.get(position);
        var image = refs.get(card.pictureAddress());
        assert image != null;
        holder.bind(card, image);
    }

    @Override
    public int getItemCount() {
        return imageCards.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageCard;
        TextView rarity;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCard = itemView.findViewById(R.id.carousel_image_card);
            rarity = itemView.findViewById(R.id.text_rarity);
        }

        public void bind(Card card, ImageCard image) {
            byte[] content = image.content();
            Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);
            imageCard.setImageBitmap(bitmap);
            rarity.setText(card.rarity().name());
        }

    }


}
