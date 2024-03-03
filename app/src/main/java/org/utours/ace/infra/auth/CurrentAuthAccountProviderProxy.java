package org.utours.ace.infra.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.auth.port.CurrentAuthAccountProvider;

import java.util.Optional;

public class CurrentAuthAccountProviderProxy implements CurrentAuthAccountProvider {

    private final CurrentAuthAccountProvider currentAuthAccountProvider;
    private final Context context;

    public CurrentAuthAccountProviderProxy(CurrentAuthAccountProvider currentAuthAccountProvider,
                                           Context context) {
        this.currentAuthAccountProvider = currentAuthAccountProvider;
        this.context = context;
    }

    @Override
    public Optional<AuthAccount> currentAuthAccount() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.contains("auth_account_uid")) {
            String uid = preferences.getString("auth_account_uid", "");
            return Optional.of(new AuthAccount(uid, "", ""));
        }

        return currentAuthAccountProvider.currentAuthAccount();
    }

}
