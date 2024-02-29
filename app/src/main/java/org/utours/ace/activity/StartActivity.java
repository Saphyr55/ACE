package org.utours.ace.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.activity.profile.ProfileActivity;
import org.utours.ace.profile.PlayerAccountManager;
import org.utours.ace.profile.port.PlayerAccountInventory;

public class StartActivity extends AppCompatActivity {

    private TextView startACETextView;
    private Button parameterStartButton;

    private PlayerAccountInventory playerAccountInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startACETextView = findViewById(R.id.textview_startAce);
        parameterStartButton = findViewById(R.id.btn_parameter);

        playerAccountInventory = PlayerAccountManager.INSTANCE
                .getPlayerAccountInventory();

        parameterStartButton.setOnClickListener(v -> openParameter());
        startACETextView.setOnClickListener(v -> redirectToGame());
    }

    private void redirectToGame() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }


    private void openParameter() {
    }


}