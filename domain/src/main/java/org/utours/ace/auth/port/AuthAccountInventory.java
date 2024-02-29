package org.utours.ace.auth.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.auth.model.AuthAccount;

import java.util.Optional;

@Port
public interface AuthAccountInventory {

    Optional<AuthAccount> getByEmail(String email);

}
