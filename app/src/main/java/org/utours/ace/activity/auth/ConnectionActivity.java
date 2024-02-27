package org.utours.ace.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.utours.ace.R;
import org.utours.ace.activity.StartActivity;
import org.utours.ace.auth.AuthenticationManager;
import org.utours.ace.auth.firebase.FirebaseAccountConnection;
import org.utours.ace.model.Account;
import org.utours.ace.port.AccountConnection;

public class ConnectionActivity extends AppCompatActivity {

    private static final String TAG = ConnectionActivity.class.getName();

    EditText emailEditText;
    EditText passwordEditText;
    Button connectButton;
    TextView registerHereTextView;
    ProgressBar progressBar;
    AccountConnection accountConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        emailEditText = findViewById(R.id.edittext_email);
        passwordEditText = findViewById(R.id.edittext_password);
        connectButton = findViewById(R.id.btn_connect);
        registerHereTextView = findViewById(R.id.textview_registerHere);
        progressBar = findViewById(R.id.progressBar);

        accountConnection = AuthenticationManager.INSTANCE
                .getAuthenticationProvider()
                .accountConnection();

        registerHereTextView.setOnClickListener(v -> redirectToRegisterActivity());
        connectButton.setOnClickListener(v -> onPressedConnectButton());
    }

    private void onPressedConnectButton() {
        var email = String.valueOf(emailEditText.getText());
        var password = String.valueOf(passwordEditText.getText());
        var account = new Account(email, password);


        if (account.email().isBlank()) {
            Toast.makeText(ConnectionActivity.this, "Unvalidated email.", Toast.LENGTH_LONG).show();
            return;
        }

        if (account.password().isBlank() || account.password().length() < 6) {
            Toast.makeText(ConnectionActivity.this, "Unvalidated password.", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        accountConnection.connect(
                account,
                this::connectionSuccess,
                this::connectionFailed
        );

        progressBar.setVisibility(View.GONE);
    }

    private void connectionFailed(Exception e) {
        Log.w(TAG, "accountConnection:failure", e);
        Toast.makeText(ConnectionActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }

    private void connectionSuccess(Account account) {
        redirectToStartActivity(account);
    }

    private void redirectToStartActivity(Account account) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

}