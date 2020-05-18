package merchant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface MerchantService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("ajax.php")
    Response prepareMarketplace(@QueryParam("cmd") String queryCmd, @FormParam("cmd") String cmd, @FormParam("r1") String lumber, @FormParam("r2") String clay, @FormParam("r3") String iron,
                                @FormParam("r4") String crop, @FormParam("dname") String dname, @FormParam("x") String x, @FormParam("y") String y,
                                @FormParam("id") String id, @FormParam("t") String t, @FormParam("x2") String x2, @FormParam("ajaxToken") String ajaxToken);

    @POST
    @Path("ajax.php")
    Response sendMerchant(@QueryParam("cmd") String queryCmd, @FormParam("cmd") String cmd, @FormParam("t") String t, @FormParam("id") String id, @FormParam("a") String a,
                          @FormParam("sz") String sz, @FormParam("kid") String kid, @FormParam("c") String c, @FormParam("x2") String x2,
                          @FormParam("r1") String lumber, @FormParam("r2") String clay, @FormParam("r3") String iron, @FormParam("r4") String crop,
                          @FormParam("ajaxToken") String ajaxToken);

    @POST
    @Path("ajax.php")
    Response getVillageListToSend(@QueryParam("cmd") String queyCmd, @FormParam("cmd") String cmd, @FormParam("type") String type,
                                  @FormParam("search") String name, @FormParam("ajaxToken") String ajaxToken);

    @GET
    @Path("/build.php")
    String getSendResourcesHtml(@QueryParam("t") String t, @QueryParam("id") String id);
}
