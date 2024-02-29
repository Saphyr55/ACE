package org.utours.ace.auth.port;

import org.utours.ace.common.annotation.Port;

@Port
public interface AuthenticationProvider {

    AuthAccountConnection accountConnection();

    AuthAccountRegistration accountRegistration();

}
