package org.utours.ace.auth.model;

import java.util.UUID;

public record AuthAccount(
        String uid,
        String email,
        String password
) {

}
