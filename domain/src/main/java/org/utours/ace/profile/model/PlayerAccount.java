package org.utours.ace.profile.model;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public record PlayerAccount(
        String uid,
        String username,
        String email,
        Integer level,
        String description
) {

}
