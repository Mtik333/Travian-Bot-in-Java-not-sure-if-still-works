package resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public interface ResourceService {

    @GET
    @Path("/build.php")
    String getUpgradeResource(@QueryParam("id") String id);

    @GET
    @Path("/dorf1.php")
    String buildUpgradeResource(@QueryParam("a") String id, @QueryParam("c") String something);

}
