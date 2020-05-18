package messages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import restutils.AbstractClient;

import javax.ws.rs.core.Response;

public class MessageClient extends AbstractClient<MessageService> {

    private static final Logger LOGGER = LogManager.getLogger(MessageClient.class);

    public MessageClient(ResteasyWebTarget stub) {
        super(stub, MessageService.class);
    }

    public Response postMessageResponse(String recipient, String subject, String message) {
        LOGGER.info("Entering postMessageResponse: recipient: " + recipient + ", subject: " + subject + ", message" + message);
        return execute(() -> service.postMessage("1", recipient, subject, message));
    }
}
