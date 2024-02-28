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
import org.utours.ace.common.AccountValidatorProvider;
import org.utours.ace.model.Account;
import org.utours.ace.port.AccountRegistration;

import java.util.Stack;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView connectHereTextView;
    private ProgressBar progressBar;

    private AccountRegistration accountRegistration;
    private AccountValidatorProvider accountValidatorProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.edittext_email);
        passwordEditText = findViewById(R.id.edittext_password);
        registerButton = findViewById(R.id.btn_register);
        connectHereTextView = findViewById(R.id.textview_connectHere);
        progressBar = findViewById(R.id.progressBar);

        accountValidatorProvider = () -> this;

        accountRegistration = AuthenticationManager.INSTANCE
                .getAuthenticationProvider()
                .accountRegistration();

        connectHereTextView.setOnClickListener(v -> redirectToConnectionActivity());
        registerButton.setOnClickListener(v -> registerAccount());
    }

    private void registerAccount() {

        var email = String.valueOf(emailEditText.getText());
        var password = String.valueOf(passwordEditText.getText());
        var account = new Account(email, password);

        boolean isInvalid = accountValidatorProvider
                .validatorOf(account)
                .trace(this::sendErrorMessage)
                .isInvalid();

        if (isInvalid) return;

        progressBar.setVisibility(View.VISIBLE);

        accountRegistration.register(account,
                this::onSuccessRegistration,
                this::onFailedRegistration);

        progressBar.setVisibility(View.GONE);
    }

    private void onFailedRegistration(Exception e) {
        Log.w(RegisterActivity.class.getName(), "CreateUserWithEmail:Failure", e);
        Toast.makeText(RegisterActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT)
                .show();
    }

    private void onSuccessRegistration(Account account) {
        Toast.makeText(RegisterActivity.this, R.string.authentication_was_a_success, Toast.LENGTH_SHORT)
                .show();
        redirectToConnectionActivity();
    }

    private void redirectToConnectionActivity() {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendErrorMessage(Account a, Stack<Throwable> throwableStack) {
        throwableStack.stream()
                .findFirst()
                .ifPresent(this::messagingUserWithToast);
    }

    private void messagingUserWithToast(Throwable throwable) {
        Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }
}