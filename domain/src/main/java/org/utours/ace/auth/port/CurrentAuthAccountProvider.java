package org.utours.ace.auth.port;

import org.utours.ace.auth.model.AuthAccount;
import org.utours.ace.common.annotation.Port;

import java.util.Optional;

@Port
public interface CurrentAuthAccountProvider {

    Optional<AuthAccount> currentAuthAccount();

}
