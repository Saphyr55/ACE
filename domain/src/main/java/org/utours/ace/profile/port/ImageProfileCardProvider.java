package org.utours.ace.profile.port;


import org.utours.ace.common.annotation.Port;
import org.utours.ace.profile.model.ProfileCardImage;

import java.util.concurrent.CompletableFuture;

@Port
public interface ImageProfileCardProvider {

    CompletableFuture<ProfileCardImage> provide(String uid);

}
