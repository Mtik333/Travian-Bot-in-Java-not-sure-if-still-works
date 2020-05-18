package login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import restutils.AbstractClient;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;

public class LoginClient extends AbstractClient<LoginService> {

    private static final Logger LOGGER = LogManager.getLogger(LoginClient.class);

    public LoginClient(ResteasyWebTarget stub) {
        super(stub, LoginService.class);
    }

    public Response loginToTravian(String username, String password) {
        LOGGER.info("Entering loginToTravian: username: " + username + ", password: " + password);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String test = String.valueOf(timestamp.getTime() / 1000);
        return execute(() -> service.getLoginResponse(username, password, "1", "Login", test));
    }

    public String getResources() {
        LOGGER.info("Entering getResources");
        return execute(() -> service.getResources());
    }

    public String getBuildings() {
        LOGGER.info("Entering getBuildings");
        return execute(() -> service.getBuildings());
    }

    public String changeVillage(String newdid) {
        LOGGER.info("Entering changeVillage: newdid: " + newdid);
        return execute(() -> service.changeVillage(newdid));
    }
}
