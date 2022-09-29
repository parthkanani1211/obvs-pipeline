package ai.obvs.websocket;

import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;

@ManagedService(path = "/sessions/{sessionId}")
public class SessionMessageHandler {

    @PathParam
    private String sessionId;

    @Ready
    public void onReady(final AtmosphereResource resource) {

    }

    @Message(decoders = {AcknowledgementDecoder.class})
    @DeliverTo(DeliverTo.DELIVER_TO.RESOURCE)
    public void onMessage(AtmosphereResource r) {

    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event){

    }

}
