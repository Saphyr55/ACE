package org.utours.ace.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import org.utours.ace.profile.dialog.CustomizeProfileDialog;
import org.utours.ace.profile.feature.CardService;
import org.utours.ace.profile.feature.GachaService;
import org.utours.ace.profile.feature.PlayerAccountService;
import org.utours.ace.profile.gacha.GachaFragment;
import org.utours.ace.profile.profile.ProfileFragment;
import org.utours.ace.shop.ShopFragment;

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

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WaitFragment waitFragment = WaitFragment.newInstance();

        replaceFragment(waitFragment);
        binding.navBar.setVisibility(View.GONE);
        binding.navBarView.setBackground(null);
        binding.navBarView.setSelectedItemId(R.id.item_main);

        PlayerAccountService playerAccountService = PlayerAccountContainer.INSTANCE.getService();
        CardService cardService = PlayerAccountContainer.INSTANCE.getCardService(this);
        GachaService gachaService = PlayerAccountContainer.INSTANCE.getGachaService(this);

        mainFragment = MainFragment.newInstance(playerAccountService, cardService, this);
        profileFragment =ProfileFragment.newInstance(this,cardService,playerAccountService);
        deckFragment = DeckFragment.newInstance(cardService, playerAccountService, this);
        gachaFragment = new GachaFragment(cardService, gachaService, this);
        shopFragment = new ShopFragment();

        cardService
                .downloadAndInstallAllImages()
                .thenCompose(unused -> deckFragment.setup())
                .thenCompose(unused -> gachaFragment.setup())
                .thenCompose(unused -> profileFragment.setup())
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

    private Void messagingErrorFragment(Throwable throwable) {
        runOnUiThread(() -> {
            replaceFragment(new ErrorFragment("Loading account failed."));
            Log.e(TAG, throwable.getMessage(), throwable);
        });
        throw new RuntimeException(throwable);
    }

    public void redirectToMain() {
        binding.navBarView.setSelectedItemId(R.id.item_main);
        replaceFragment(mainFragment);
    }

    public void redirectToProfile() {
        binding.navBarView.setSelectedItemId(R.id.item_profile);
        replaceFragment(profileFragment);
    }

    public void redirectToDeck() {
        binding.navBarView.setSelectedItemId(R.id.item_deck);
        replaceFragment(deckFragment);
    }

    public void redirectToGacha() {
        binding.navBarView.setSelectedItemId(R.id.item_gacha);
        replaceFragment(gachaFragment);
    }

    public void redirectToShop() {
        binding.navBarView.setSelectedItemId(R.id.item_shop);
        replaceFragment(shopFragment);
    }

    public void replaceFragment(Fragment fragment) {
        runOnUiThread(() -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        });
    }

    public void fullyReplaceFragment(Fragment fragment) {
        runOnUiThread(() -> binding.navBar.setVisibility(View.GONE));
        replaceFragment(fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CustomizeProfileDialog.REQUEST_CODE) {
                Uri uri = data.getData();
                Bitmap bitmap = profileFragment.loadFromUri(uri);
                profileFragment.changeView(bitmap);
            }
        }
    }




}