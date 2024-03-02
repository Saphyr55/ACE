package org.utours.ace.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.utours.ace.R;
import org.utours.ace.databinding.ActivityNavigationBinding;
import org.utours.ace.fragment.deck.DeckFragment;
import org.utours.ace.fragment.gacha.GachaFragment;
import org.utours.ace.fragment.main.MainFragment;
import org.utours.ace.fragment.profile.ProfileFragment;
import org.utours.ace.fragment.shop.ShopFragment;

public class NavigationActivity extends AppCompatActivity {

    ActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new MainFragment());
        binding.navBarView.setBackground(null);

        binding.navBarView.setOnItemSelectedListener(this::switchBetweenFragment);
    }

    private boolean switchBetweenFragment(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.item_main) {
            replaceFragment(new MainFragment());
        } else if (menuItem.getItemId() == R.id.item_profile) {
            replaceFragment(new ProfileFragment());
        } else if (menuItem.getItemId() == R.id.item_deck) {
            replaceFragment(new DeckFragment());
        } else if (menuItem.getItemId() == R.id.item_gacha) {
            replaceFragment(new GachaFragment());
        } else if (menuItem.getItemId() == R.id.item_shop) {
            replaceFragment(new ShopFragment());
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