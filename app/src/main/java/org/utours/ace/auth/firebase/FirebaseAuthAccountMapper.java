package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseUser;

import org.utours.ace.auth.model.AuthAccount;

public class FirebaseAuthAccountMapper {


    public AuthAccount map(FirebaseUser user, String password) {
        if (user == null) return null;
        return new AuthAccount(user.getUid(), user.getEmail(), password);
    }

    public AuthAccount map(FirebaseUser user) {
        return map(user, "");
    }

}
