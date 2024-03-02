package org.utours.ace.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.utours.ace.R;
import org.utours.ace.databinding.ActivityNavigationBinding;
import org.utours.ace.profile.DeckFragment;
import org.utours.ace.gacha.GachaFragment;
import org.utours.ace.profile.ProfileFragment;
import org.utours.ace.shop.ShopFragment;

public class NavigationActivity extends AppCompatActivity {

    ActivityNavigationBinding binding;
    MainFragment mainFragment;
    ProfileFragment profileFragment;
    DeckFragment deckFragment;
    GachaFragment gachaFragment;
    ShopFragment shopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainFragment = new MainFragment();
        profileFragment = new ProfileFragment();
        deckFragment = new DeckFragment();
        gachaFragment = new GachaFragment();
        shopFragment = new ShopFragment();

        binding.navBarView.setBackground(null);
        binding.navBarView.setOnItemSelectedListener(this::switchBetweenFragment);

        replaceFragment(mainFragment);
        binding.navBarView.setSelectedItemId(R.id.item_main);
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

}