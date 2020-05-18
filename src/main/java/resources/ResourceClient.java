package resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import restutils.AbstractClient;

public class ResourceClient extends AbstractClient<ResourceService> {

    private static final Logger LOGGER = LogManager.getLogger(ResourceClient.class);

    public ResourceClient(ResteasyWebTarget stub) {
        super(stub, ResourceService.class);
    }

    public String getResourceInfo(String id) {
        LOGGER.info("Entering getResourceInfo: id: " + id);
        return execute(() -> service.getUpgradeResource(id));
    }

    public String getUpgradeResource(String a, String c) {
        LOGGER.info("Entering getUpgradeResource: a: " + a + ", c: " + c);
        return execute(() -> service.buildUpgradeResource(a, c));
    }

}
