package restutils;

import buildings.BuildingClient;
import login.LoginClient;
import merchant.MerchantClient;
import messages.MessageClient;
import military.MilitaryClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import resources.ResourceClient;

public class ClientFactory {

    private static final Logger LOGGER = LogManager.getLogger(ClientFactory.class);
    private final LoginClient loginClient;
    private final ResourceClient resourceClient;
    private final MessageClient messageClient;
    private final BuildingClient buildingClient;
    private final MilitaryClient militaryClient;
    private final MerchantClient merchantClient;

    public ClientFactory() {
        LOGGER.info("Creating ClientFactory");
        ResteasyWebTarget stub = RestEasyClient.createRestEasyObj();
        if (stub != null) {
            loginClient = new LoginClient(stub);
            resourceClient = new ResourceClient(stub);
            messageClient = new MessageClient(stub);
            buildingClient = new BuildingClient(stub);
            militaryClient = new MilitaryClient(stub);
            merchantClient = new MerchantClient(stub);
        } else {
            loginClient = null;
            resourceClient = null;
            messageClient = null;
            militaryClient = null;
            buildingClient = null;
            merchantClient = null;
        }
    }

    public LoginClient getLoginClient() {
        return loginClient;
    }

    ResourceClient getResourceClient() {
        return resourceClient;
    }

    MessageClient getMessageClient() {
        return messageClient;
    }

    BuildingClient getBuildingClient() {
        return buildingClient;
    }

    MilitaryClient getMilitaryClient() {
        return militaryClient;
    }

    MerchantClient getMerchantClient() {
        return merchantClient;
    }
}
