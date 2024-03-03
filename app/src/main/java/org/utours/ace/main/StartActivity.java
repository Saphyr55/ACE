package org.utours.ace.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.utours.ace.R;
import org.utours.ace.infra.auth.AuthenticationContainer;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.infra.profile.PlayerAccountContainer;
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
                .getService(this)
                .currentAuthAccount()
                .orElseThrow(RuntimeException::new);

        PlayerAccountContainer.INSTANCE
                .getPlayerAccountRepository()
                .getByUID(authAccount.uid())
                .thenAccept(opt -> initPlayerAccount(opt.orElseThrow(RuntimeException::new)));
    }

    private void initPlayerAccount(PlayerAccount playerAccount) {
        PlayerAccountContainer.INSTANCE.setCurrentPlayerAccount(playerAccount);
        parameterStartButton.setOnClickListener(v -> openParameter());
        startACETextView.setOnClickListener(v -> redirectToNavigationActivity());
    }

    private void redirectToNavigationActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    private void openParameter() {

    }


}