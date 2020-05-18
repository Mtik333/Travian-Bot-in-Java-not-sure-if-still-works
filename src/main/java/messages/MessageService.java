package messages;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface MessageService {

    @POST
    @Path("messages.php")
    Response postMessage(@QueryParam("t") String t, @FormParam("an") String recipient, @FormParam("be") String subject, @FormParam("message") String message);

    @GET
    @Path("messages.php")
    String showInbox(@QueryParam("t") String t);
}
