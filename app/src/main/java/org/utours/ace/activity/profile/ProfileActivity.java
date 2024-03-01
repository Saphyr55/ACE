package org.utours.ace.activity.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.activity.auth.ConnectionActivity;
import org.utours.ace.profile.PlayerAccountContainer;
import org.utours.ace.profile.model.PlayerAccount;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ConnectionActivity.class.getName();

    TextView usernameTextView;
    TextView levelTextView;
    EditText descriptionEditText;
    PlayerAccount playerAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.text_username);
        levelTextView = findViewById(R.id.text_level);
        descriptionEditText = findViewById(R.id.edittext_description);

        playerAccount = PlayerAccountContainer.INSTANCE
                .getCurrentPlayerAccount()
                .orElseThrow(ProfileActivity::errorMissingCurrentPlayer);

        usernameTextView.setText(playerAccount.username());
        descriptionEditText.setText(playerAccount.description());

        levelTextView.setText(String.format("Level %s", playerAccount.level()));
    }


    private static RuntimeException errorMissingCurrentPlayer() {
        var msg = "Current player account doesn't exist, and entire in ProfileActivity.";
        var e = new RuntimeException(msg);
        Log.e(TAG, e.getMessage(), e);
        return e;
    }


}