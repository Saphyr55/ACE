package org.utours.ace.profile.gacha;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;

import org.utours.ace.R;
import org.utours.ace.common.BackConfirmDialog;
import org.utours.ace.infra.profile.PlayerAccountContainer;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.GachaService;
import org.utours.ace.profile.model.Card;
import org.utours.ace.profile.model.ImageCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GachaPullActivity extends AppCompatActivity {

    private RecyclerView carouselTopCards;
    private GachaCarouselAdapter carouselAdapter;
    private static List<Card> cards;
    private static Map<String, ImageCard> imageCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_gacha_pull);
        setLayoutPadding();
        setupCarousel();

        showConfirmDialogOnPressedBack();

        TextView textView = findViewById(R.id.text_tap_to_back);

        textView.setOnClickListener(v -> finish());
    }

    private void setLayoutPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showConfirmDialogOnPressedBack() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                var dialog = new BackConfirmDialog(GachaPullActivity.this);
                dialog.setOnConfirmButton(() -> finish());
                dialog.show();
            }
        });
    }

    private void setupCarousel() {
        
        carouselTopCards = findViewById(R.id.carousel_top_cards);
        carouselAdapter = new GachaCarouselAdapter(cards, imageCards);
        var carouselLayoutManager = new CarouselLayoutManager(new MultiBrowseCarouselStrategy());
        var carouselSnapHelper = new CarouselSnapHelper();

        carouselSnapHelper.attachToRecyclerView(carouselTopCards);

        carouselTopCards.setLayoutManager(carouselLayoutManager);
        carouselTopCards.setAdapter(carouselAdapter);
    }

    public static void setImageCards(List<ImageCard> imageCards) {
        var result = new HashMap<String, ImageCard>();
        for (int i = 0; i < imageCards.size(); i++) {
            Card card = cards.get(i);
            result.put(card.pictureId(), imageCards.get(i));
        }
        GachaPullActivity.imageCards = result;
    }

    public static List<Card> getCards() {
        return cards;
    }

    public static void setCards(List<Card> cards) {
        GachaPullActivity.cards = cards;
    }

    @Override
    protected void onDestroy() {
        cards = null;
        imageCards = null;
        super.onDestroy();
    }
}