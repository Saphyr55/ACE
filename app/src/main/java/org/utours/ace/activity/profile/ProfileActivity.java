package org.utours.ace.activity.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.auth.AuthenticationContainer;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.profile.PlayerAccountContainer;
import org.utours.ace.profile.model.PlayerAccount;

public class ProfileActivity extends AppCompatActivity {

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
                .orElseThrow(RuntimeException::new);

        usernameTextView.setText(playerAccount.username());
        descriptionEditText.setText(playerAccount.description());

        var level = String.format("Level %s", playerAccount.level());
        levelTextView.setText(level);
    }


}