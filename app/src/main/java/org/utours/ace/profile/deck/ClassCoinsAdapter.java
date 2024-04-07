package org.utours.ace.profile.deck;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import org.utours.ace.R;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ClassCoin;
import org.utours.ace.profile.port.ClassCoinInformation;

import java.util.List;

public class ClassCoinsAdapter
        extends RecyclerView.Adapter<ClassCoinsAdapter.ClassCoinViewHolder> {

    private final List<ClassCoin> classCoins;
    private final ClassCoin cardClassCoin;
    private final ClassCoinInformation<Bitmap> classCoinInformation;
    private final OnClickCassCoin onClickClassCoin;

    public ClassCoinsAdapter(List<ClassCoin> classCoins,
                             ClassCoin cardClassCoin,
                             ClassCoinInformation<Bitmap> classCoinInformation,
                             OnClickCassCoin onClickClassCoin) {

        this.classCoins = classCoins;
        this.cardClassCoin = cardClassCoin;
        this.classCoinInformation = classCoinInformation;
        this.onClickClassCoin = onClickClassCoin;
    }

    @NonNull
    @Override
    public ClassCoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_class_coin, parent, false);
        return new ClassCoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassCoinViewHolder holder, int position) {
        ClassCoin classCoin = classCoins.get(position);
        holder.bind(classCoin, cardClassCoin, classCoinInformation, onClickClassCoin);
    }

    @Override
    public int getItemCount() {
        return classCoins.size();
    }

    public static class ClassCoinViewHolder extends RecyclerView.ViewHolder {

        private final View arrowImage;
        private final TextView infoText;
        private final View itemView;
        private final AppCompatImageView classCoinImage;

        public ClassCoinViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.infoText = itemView.findViewById(R.id.text_info);
            this.classCoinImage = itemView.findViewById(R.id.image_classCoin);
            this.arrowImage = itemView.findViewById(R.id.image_arrow);
        }

        public void bind(ClassCoin classCoin,
                         ClassCoin cardClassCoin,
                         ClassCoinInformation<Bitmap> information,
                         OnClickCassCoin onClickClassCoin) {

            if (cardClassCoin.equals(classCoin)) {
                arrowImage.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(v -> onClickClassCoin.onClick(classCoin, v));
            String info = information.information(classCoin.type());
            infoText.setText(info);
            classCoinImage.setImageBitmap(information.image(classCoin.type()));
        }

    }

}
