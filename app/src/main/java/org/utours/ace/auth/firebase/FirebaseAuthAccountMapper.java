package org.utours.ace.auth.firebase;

import com.google.firebase.auth.FirebaseUser;

import org.utours.ace.auth.model.AuthAccount;

class FirebaseAuthAccountMapper {


    AuthAccount map(FirebaseUser user, String password) {
        return new AuthAccount(user.getUid(), user.getEmail(), password);
    }

}
