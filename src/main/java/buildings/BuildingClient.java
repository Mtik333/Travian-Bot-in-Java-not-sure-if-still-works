package buildings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import restutils.AbstractClient;

import javax.ws.rs.core.Response;

public class BuildingClient extends AbstractClient<BuildingService> {

    private static final Logger LOGGER = LogManager.getLogger(BuildingClient.class);

    public BuildingClient(ResteasyWebTarget stub) {
        super(stub, BuildingService.class);
    }

    public String getBuildingInfo(String id) {
        LOGGER.info("Entering getBuildingInfo: id: " + id);
        return execute(() -> service.getUpgradeBuilding(id));
    }

    public String getUpgradeBuilding(String[] params) {
        LOGGER.info("Entering getUpgradeBuilding: params: " + params[0]);
        return execute(() -> service.buildUpgradeBuilding(params[0], params[1]));
    }

    public String getNewBuildingCategory(String id, String category) {
        LOGGER.info("Entering getNewBuildingCategory: id: " + id + ", category: " + category);
        return execute(() -> service.getNewBuildingCategory(id, category));
    }

    public String buildNewBuilding(String type, String id, String c) {
        LOGGER.info("Entering buildNewBuilding: type: " + type + ", id: " + id + ", c: " + c);
        return execute(() -> service.buildNewBuilding(type, id, c));
    }

    public Response cancelTask(String d, String a, String c) {
        LOGGER.info("Entering cancelTask: d: " + d + ", a: " + a + ", c: " + c);
        return execute(() -> service.cancelTaskBuilding(d, a, c));
    }
}
