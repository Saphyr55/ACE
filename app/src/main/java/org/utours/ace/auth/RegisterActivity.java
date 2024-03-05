package org.utours.ace.auth;

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
import org.utours.ace.auth.feature.AuthAccountService;
import org.utours.ace.infra.auth.AuthenticationContainer;
import org.utours.ace.infra.auth.AuthAccountValidatorProvider;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.infra.profile.PlayerAccountContainer;
import org.utours.ace.profile.model.PlayerAccount;
import org.utours.ace.profile.port.PlayerAccountRepository;

import java.util.Stack;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView connectHereTextView;
    private ProgressBar progressBar;
    private AuthAccountService authAccountService;
    private PlayerAccountRepository playerAccountRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authAccountService = AuthenticationContainer.INSTANCE.getService(this);
        playerAccountRepository = PlayerAccountContainer.INSTANCE.getPlayerAccountRepository();

        emailEditText = findViewById(R.id.edittext_email);
        passwordEditText = findViewById(R.id.edittext_password);
        registerButton = findViewById(R.id.btn_register);
        connectHereTextView = findViewById(R.id.textview_connectHere);
        progressBar = findViewById(R.id.progressBar);

        connectHereTextView.setOnClickListener(v -> redirectToConnectionActivity());
        registerButton.setOnClickListener(v -> registerAccount());
    }

    private void registerAccount() {
        progressBar.setVisibility(View.VISIBLE);

        var email = String.valueOf(emailEditText.getText());
        var password = String.valueOf(passwordEditText.getText());
        var authAccount = new AuthAccount(String.valueOf(UUID.randomUUID()), email, password);

        boolean isValid = AuthAccountValidatorProvider
                .validatorOf(this, authAccount)
                .trace(this::sendErrorMessage)
                .isValid();

        if (isValid) {
            authAccountService.register(authAccount,
                    this::onSuccessRegistration,
                    this::onFailedRegistration);
        }
    }

    private void onFailedRegistration(Exception e) {
        Log.w(RegisterActivity.class.getName(), "CreateUserWithEmail:Failure", e);
        Toast.makeText(RegisterActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    private void onSuccessRegistration(AuthAccount account) {
        var playerAccountBuild = PlayerAccount.Builder.of(account.uid(), account.email());
        playerAccountRepository.store(playerAccountBuild.build());
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