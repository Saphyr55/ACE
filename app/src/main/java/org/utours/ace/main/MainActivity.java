package org.utours.ace.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;

import org.utours.ace.auth.ConnectionActivity;
import org.utours.ace.infra.profile.PlayerAccountContainer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup database.
        PlayerAccountContainer.INSTANCE.getCardService(this);

        var cls = determineActivityClass();
        var intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    private Class<?> determineActivityClass() {

        var preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains("auth_account_uid")) {
            return StartActivity.class;
        }

        return ConnectionActivity.class;
    }

}