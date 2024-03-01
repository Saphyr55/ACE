package org.utours.ace.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.activity.profile.ProfileActivity;
import org.utours.ace.auth.AuthenticationContainer;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.profile.PlayerAccountContainer;
import org.utours.ace.profile.model.PlayerAccount;

public class StartActivity extends AppCompatActivity {

    private TextView startACETextView;
    private Button parameterStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startACETextView = findViewById(R.id.textview_startAce);
        parameterStartButton = findViewById(R.id.btn_parameter);

        AuthAccount authAccount = AuthenticationContainer.INSTANCE
                .getCurrentAuthAccountProvider()
                .currentAuthAccount()
                .orElseThrow(RuntimeException::new);

        PlayerAccountContainer.INSTANCE
                .getPlayerAccountRepository()
                .getByUID(authAccount.uid())
                .thenAccept(opt ->{
                    PlayerAccount playerAccount = opt.orElseThrow(RuntimeException::new);
                    initPlayerAccount(playerAccount);
                });
    }

    private void initPlayerAccount(PlayerAccount playerAccount) {
        PlayerAccountContainer.INSTANCE.setCurrentPlayerAccount(playerAccount);
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