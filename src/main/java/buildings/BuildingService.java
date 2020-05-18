package buildings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public interface BuildingService {

    @GET
    @Path("/build.php")
    String getUpgradeBuilding(@QueryParam("id") String id);

    @GET
    @Path("/dorf2.php")
    String buildUpgradeBuilding(@QueryParam("a") String id, @QueryParam("c") String something);

    @GET
    @Path("/build.php")
    String getNewBuildingCategory(@QueryParam("id") String id, @QueryParam("category") String category);

    @GET
    @Path("/dorf2.php")
    String buildNewBuilding(@QueryParam("a") String type, @QueryParam("id") String id, @QueryParam("c") String c);

    @GET
    @Path("/build.php")
    String getSendResourcesHtml(@QueryParam("t") String t, @QueryParam("id") String id);

    @GET
    @Path("/dorf2.php")
    Response cancelTaskBuilding(@QueryParam("d") String d, @QueryParam("a") String a, @QueryParam("c") String c);

}
