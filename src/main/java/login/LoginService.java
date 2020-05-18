package login;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


public interface LoginService {

    @POST
    @Path("/dorf1.php")
    Response getLoginResponse(@FormParam("name") String name, @FormParam("password") String password, @FormParam("lowRes") String lowRes, @FormParam("s1") String s1, @FormParam("login") String timestamp);

    @GET
    @Path("/dorf1.php")
    String getResources();

    @GET
    @Path("/dorf2.php")
    String getBuildings();

    @GET
    @Path("/dorf1.php")
    String changeVillage(@QueryParam("newdid") String newdid);

}
