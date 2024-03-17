package org.utours.ace.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import org.utours.ace.R;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

public class PlayerCardView {

    private final AppCompatImageView cardImageView;
    private final TextView attackPointText;
    private final TextView lifePointText;
    private final TextView manaPointText;
    private final TextView rarityText;

    public PlayerCardView(@NonNull View itemView) {
        this.cardImageView = itemView.findViewById(R.id.image_card);
        this.attackPointText = itemView.findViewById(R.id.text_attackPoint);
        this.lifePointText = itemView.findViewById(R.id.text_lifePoint);
        this.manaPointText = itemView.findViewById(R.id.text_manaCost);
        this.rarityText = itemView.findViewById(R.id.text_rarity);
    }

    public void bind(Card card, ImageCard imageCard) {
        byte[] content = imageCard.content();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);

        String attackPointStr = card.attackPoint() + " AP";
        String manaPointStr = card.manaPoint() + " MP";
        String lifePointStr = card.lifePoint() + " LP";
        String rarityStr = card.rarity().name();

        attackPointText.setText(attackPointStr);
        manaPointText.setText(manaPointStr);
        lifePointText.setText(lifePointStr);
        rarityText.setText(rarityStr);
        cardImageView.setImageBitmap(bitmap);
    }

}
