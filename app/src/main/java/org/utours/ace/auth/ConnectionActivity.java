package org.utours.ace.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.utours.ace.main.StartActivity;
import org.utours.ace.infra.auth.AuthenticationContainer;
import org.utours.ace.infra.auth.AuthAccountValidatorProvider;
import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.AuthAccountConnection;

import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import java.util.function.Consumer;


public class ConnectionActivity extends AppCompatActivity {

    private static final String TAG = ConnectionActivity.class.getName();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button connectButton;
    private TextView registerHereTextView;
    private ProgressBar progressBar;
    private AuthAccountService authAccountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        authAccountService = AuthenticationContainer.INSTANCE.getService(this);

        emailEditText = findViewById(R.id.edittext_email);
        passwordEditText = findViewById(R.id.edittext_password);
        connectButton = findViewById(R.id.btn_connect);
        registerHereTextView = findViewById(R.id.textview_registerHere);
        progressBar = findViewById(R.id.progressBar);

        registerHereTextView.setOnClickListener(v -> redirectTo(RegisterActivity.class));
        connectButton.setOnClickListener(v -> tryToConnect());
    }

    private void tryToConnect() {
        progressBar.setVisibility(View.VISIBLE);

        var email = String.valueOf(emailEditText.getText());
        var password = String.valueOf(passwordEditText.getText());
        var account = new AuthAccount(UUID.randomUUID().toString(), email, password);

        AuthAccountValidatorProvider
                .validatorOf(this, account)
                .ifValid(this::connectAccount)
                .trace(this::sendErrorMessageOnInvalidation);
    }

    private void connectAccount(AuthAccount authAccount) {
        authAccountService.connect(authAccount,
                this::onConnectionSuccess,
                this::sendErrorMessageOnAuthentication);
    }

    private void onConnectionSuccess(AuthAccount authAccount) {
        var preferences = PreferenceManager.getDefaultSharedPreferences(this);
        var editor = preferences.edit();
        editor.putString("auth_account_uid", authAccount.uid());
        editor.apply();
        redirectTo(StartActivity.class);
    }

    private void sendErrorMessageOnInvalidation(AuthAccount a,
                                                Stack<Throwable> throwableStack) {
        throwableStack.stream().findFirst().ifPresent(throwable -> {
            String msg = throwable.getMessage();
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        });
    }

    private void sendErrorMessageOnAuthentication(Exception e) {
        Log.w(TAG, "AccountConnection:Failure", e);
        Toast.makeText(this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    private void redirectTo(Class<? extends Activity> cls, Consumer<Intent> before) {

        Intent intent = new Intent(this, cls);
        before.accept(intent);
        startActivity(intent);
        finish();
    }

    private void redirectTo(Class<? extends Activity> cls) {
        redirectTo(cls, intent -> {});
    }

}