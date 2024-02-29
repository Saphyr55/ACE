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

import org.utours.ace.R;
import org.utours.ace.auth.AuthenticationManager;
import org.utours.ace.auth.AuthAccountValidatorProvider;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountRegistration;
import org.utours.ace.profile.PlayerAccountManager;
import org.utours.ace.profile.feature.PlayerAccountProvider;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountInventory;

import java.util.Stack;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView connectHereTextView;
    private ProgressBar progressBar;

    private AuthAccountRegistration authAccountRegistration;
    private AuthAccountValidatorProvider authAccountValidatorProvider;
    private PlayerAccountInventory playerAccountInventory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.edittext_email);
        passwordEditText = findViewById(R.id.edittext_password);
        registerButton = findViewById(R.id.btn_register);
        connectHereTextView = findViewById(R.id.textview_connectHere);
        progressBar = findViewById(R.id.progressBar);

        authAccountValidatorProvider = () -> this;
        authAccountRegistration = AuthenticationManager.INSTANCE
                .getAuthenticationProvider()
                .accountRegistration();

        playerAccountInventory = PlayerAccountManager.INSTANCE
                .getPlayerAccountInventory();

        connectHereTextView.setOnClickListener(v -> redirectToConnectionActivity());
        registerButton.setOnClickListener(v -> registerAccount());
    }

    private void registerAccount() {
        var email = String.valueOf(emailEditText.getText());
        var password = String.valueOf(passwordEditText.getText());
        var authAccount = new AuthAccount(String.valueOf(UUID.randomUUID()), email, password);

        boolean isInvalid = authAccountValidatorProvider
                .validatorOf(authAccount)
                .trace(this::sendErrorMessage)
                .isInvalid();

        if (isInvalid) return;

        progressBar.setVisibility(View.VISIBLE);
        authAccountRegistration.register(authAccount, this::onSuccessRegistration, this::onFailedRegistration);
        progressBar.setVisibility(View.GONE);
    }

    private void onFailedRegistration(Exception e) {
        Log.w(RegisterActivity.class.getName(), "CreateUserWithEmail:Failure", e);
        Toast.makeText(RegisterActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
    }

    private void onSuccessRegistration(AuthAccount authAccount) {
        PlayerAccount playerAccount = PlayerAccountProvider.DEFAULT.provide(authAccount.uid(), authAccount.email());
        playerAccountInventory.store(playerAccount);
        redirectToConnectionActivity();
    }

    private void redirectToConnectionActivity() {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendErrorMessage(AuthAccount a, Stack<Throwable> throwableStack) {
        throwableStack.stream()
                .findFirst()
                .ifPresent(this::messagingUserWithToast);
    }

    private void messagingUserWithToast(Throwable throwable) {
        Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

}