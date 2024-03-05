package org.utours.ace.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.utours.ace.R;
import org.utours.ace.databinding.ActivityNavigationBinding;
import org.utours.ace.infra.profile.PlayerAccountContainer;
import org.utours.ace.main.ErrorFragment;
import org.utours.ace.main.MainFragment;
import org.utours.ace.main.WaitFragment;
import org.utours.ace.profile.deck.DeckFragment;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.GachaService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.gacha.GachaFragment;
import org.utours.ace.profile.profile.ProfileFragment;
import org.utours.ace.shop.ShopFragment;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = NavigationActivity.class.getName();

    private ActivityNavigationBinding binding;
    private MainFragment mainFragment;
    private ProfileFragment profileFragment;
    private DeckFragment deckFragment;
    private GachaFragment gachaFragment;
    private ShopFragment shopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setDefaultPadding();

        replaceFragment(new WaitFragment());
        binding.navBar.setVisibility(View.GONE);
        binding.navBarView.setBackground(null);
        binding.navBarView.setSelectedItemId(R.id.item_main);

        PlayerAccountService playerAccountService = PlayerAccountContainer.INSTANCE.getService();
        CardService cardService = PlayerAccountContainer.INSTANCE.getCardService(this);
        GachaService gachaService = PlayerAccountContainer.INSTANCE.getGachaService(this);

        mainFragment = new MainFragment();
        profileFragment = new ProfileFragment();
        deckFragment = new DeckFragment(cardService, playerAccountService);
        gachaFragment = new GachaFragment(cardService, gachaService);
        shopFragment = new ShopFragment();

        cardService
                .downloadAndInstallAllImages()
                .thenCompose(unused -> deckFragment.setup())
                .thenCompose(unused -> gachaFragment.setup())
                .thenRun(this::startNavigationMain)
                .exceptionally(this::messagingErrorFragment);
    }

    private void setDefaultPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startNavigationMain() {
        runOnUiThread(() -> {
            binding.navBar.setVisibility(View.VISIBLE);
            binding.navBarView.setOnItemSelectedListener(this::switchBetweenFragment);
            replaceFragment(mainFragment);
        });
    }

    private boolean switchBetweenFragment(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.item_main) {
            replaceFragment(mainFragment);
        } else if (menuItem.getItemId() == R.id.item_profile) {
            replaceFragment(profileFragment);
        } else if (menuItem.getItemId() == R.id.item_deck) {
            replaceFragment(deckFragment);
        } else if (menuItem.getItemId() == R.id.item_gacha) {
            replaceFragment(gachaFragment);
        } else if (menuItem.getItemId() == R.id.item_shop) {
            replaceFragment(shopFragment);
        }

        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private Void messagingErrorFragment(Throwable throwable) {
        runOnUiThread(() -> {
            replaceFragment(new ErrorFragment("Loading account failed."));
            Log.e(TAG, throwable.getMessage(), throwable);
        });
        throw new RuntimeException(throwable);
    }

}