package org.utours.ace.auth.model;


public record AuthAccount(
        String uid,
        String email,
        String password
) {

}
