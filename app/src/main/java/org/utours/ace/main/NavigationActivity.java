package org.utours.ace.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.utours.ace.R;
import org.utours.ace.databinding.ActivityNavigationBinding;
import org.utours.ace.infra.profile.PlayerAccountContainer;
import org.utours.ace.profile.DeckFragment;
import org.utours.ace.gacha.GachaFragment;
import org.utours.ace.profile.ProfileFragment;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.shop.ShopFragment;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = NavigationActivity.class.getName();

    private ActivityNavigationBinding binding;
    private WaitFragment waitFragment;
    private MainFragment mainFragment;
    private ProfileFragment profileFragment;
    private DeckFragment deckFragment;
    private GachaFragment gachaFragment;
    private ShopFragment shopFragment;
    private CardService cardService;
    private PlayerAccountService playerAccountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        waitFragment = new WaitFragment();
        replaceFragment(waitFragment);

        cardService = PlayerAccountContainer.INSTANCE
                .getCardService(this);

        playerAccountService = PlayerAccountContainer.INSTANCE.getService();

        mainFragment = new MainFragment();
        profileFragment = new ProfileFragment();
        deckFragment = new DeckFragment(cardService, playerAccountService);
        gachaFragment = new GachaFragment();
        shopFragment = new ShopFragment();

        binding.navBar.setVisibility(View.GONE);
        binding.navBarView.setBackground(null);
        binding.navBarView.setSelectedItemId(R.id.item_main);

        deckFragment.setup()
                .thenRun(this::startNavigationMain)
                .exceptionally(this::messagingErrorFragment);
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

    private RuntimeException errorMissingCurrentPlayer() {
        var msg = "Current player account doesn't exist, and entire in ProfileActivity.";
        var e = new RuntimeException(msg);
        Log.e(TAG, e.getMessage(), e);
        return e;
    }

    private Void messagingErrorFragment(Throwable throwable) {
        runOnUiThread(() -> {
            replaceFragment(new ErrorFragment("Loading account failed."));
            Log.e(TAG, throwable.getMessage(), throwable);
        });
        throw new RuntimeException(throwable);
    }

}