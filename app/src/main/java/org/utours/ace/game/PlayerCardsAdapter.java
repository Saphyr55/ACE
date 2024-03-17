package org.utours.ace.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import org.utours.ace.R;
import org.utours.ace.game.model.Player;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.Map;
import java.util.function.Function;


public class PlayerCardsAdapter
        extends RecyclerView.Adapter<PlayerCardsAdapter.PlayerCardsViewHolder> {

    private Player player;
    private final Map<String, ImageCard> imageCardReferences;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param player Player containing the data to populate views to be used by RecyclerView.
     */
    public PlayerCardsAdapter(Player player, Map<String, ImageCard> imageCardReferences) {
        this.player = player;
        this.imageCardReferences = imageCardReferences;
    }

    public void updatePlayer(Function<Player, Player> function) {
        player = function.apply(player);
    }

    @NonNull
    @Override
    public PlayerCardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_game_card, parent, false);
        return new PlayerCardsViewHolder(view, imageCardReferences);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerCardsViewHolder holder, int position) {
        holder.bind(player.hand().get(position));
    }

    @Override
    public int getItemCount() {
        return player.hand().size();
    }

    public static class PlayerCardsViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView cardImageView;
        private TextView attackPointText;
        private TextView lifePointText;
        private TextView manaPointText;
        private TextView rarityText;

        private final Map<String, ImageCard> imageCardReferences;

        public PlayerCardsViewHolder(@NonNull View itemView, Map<String, ImageCard> imageCardReferences) {
            super(itemView);
            this.cardImageView = itemView.findViewById(R.id.image_card);
            this.attackPointText = itemView.findViewById(R.id.text_attackPoint);
            this.lifePointText = itemView.findViewById(R.id.text_lifePoint);
            this.manaPointText = itemView.findViewById(R.id.text_manaCost);
            this.rarityText = itemView.findViewById(R.id.text_rarity);
            this.imageCardReferences = imageCardReferences;
        }

        void bind(Card card) {

            String attackPointStr = card.attackPoint() + " AP";
            String manaPointStr = card.manaPoint() + " MP";
            String lifePointStr = card.lifePoint() + " LP";
            String rarityStr = card.rarity().name();

            attackPointText.setText(attackPointStr);
            manaPointText.setText(manaPointStr);
            lifePointText.setText(lifePointStr);
            rarityText.setText(rarityStr);

            ImageCard imageCard = imageCardReferences.get(card.pictureId());
            assert imageCard != null;
            byte[] content = imageCard.content();
            Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);

            cardImageView.setImageBitmap(bitmap);
        }


    }

}
