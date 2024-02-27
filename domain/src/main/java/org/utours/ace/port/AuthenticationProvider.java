package org.utours.ace.port;

import org.utours.ace.common.annotation.Port;

@Port
public interface AuthenticationProvider {

    AccountConnection accountConnection();

    AccountRegistration accountRegistration();

}
