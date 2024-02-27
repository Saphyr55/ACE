package org.utours.ace.port;

import org.utours.ace.common.annotation.Port;
import org.utours.ace.model.Account;


@Port
public interface AccountConnection {

    void connect(Account account);

    boolean isConnected(Account account);

}

