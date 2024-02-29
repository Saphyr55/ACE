package org.utours.ace.activity.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.profile.PlayerAccountManager;

public class ProfileActivity extends AppCompatActivity {

    TextView usernameTextView;
    TextView levelTextView;
    EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.text_username);
        levelTextView = findViewById(R.id.text_level);
        descriptionEditText = findViewById(R.id.edittext_description);
    }

}