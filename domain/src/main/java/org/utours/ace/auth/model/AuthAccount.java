package org.utours.ace.auth.model;

import java.io.Serializable;

public record AuthAccount(
        String uid,
        String email,
        String password
) implements Serializable {

}
