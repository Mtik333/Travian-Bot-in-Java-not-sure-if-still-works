package military;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface MilitaryService {

    @GET
    @Path("/build.php")
    String getBarracksBuilding(@QueryParam("id") String id);

    @POST
    @Path("/build.php")
        //t1 number of Phalanx to train, t2 number swordsman etc, s1 button, z + a + s to hidden inputs
    Response trainTroops(@QueryParam("id") String id, @FormParam("z") String z, @FormParam("a") String a, @FormParam("s") String s,
                         @FormParam("t1") String t1, @FormParam("t2") String t2, @FormParam("s1") String s1);

    @POST
    @Path("/build.php")
    Response trainTroopsStable(@QueryParam("id") String id, @FormParam("z") String z, @FormParam("a") String a, @FormParam("s") String s,
                               @FormParam("t3") String t3, @FormParam("t4") String t4, @FormParam("s1") String s1);

    @POST
    @Path("/build.php")
    String prepareTroopsForAttack(@QueryParam("gid") String gid, @QueryParam("tt") String tt, @FormParam("timestamp") String timestamp,
                                  @FormParam("timestamp_checksum") String checksum, @FormParam("b") String b, @FormParam("currentDid") String currentDid,
                                  @FormParam("t1") String t1, @FormParam("t2") String t2, @FormParam("t3") String t3, @FormParam("t4") String t4,
                                  @FormParam("t5") String t5, @FormParam("t6") String t6, @FormParam("t7") String t7, @FormParam("t8") String t8,
                                  @FormParam("t9") String t9, @FormParam("t10") String t10, @FormParam("t11") String t11, @FormParam("dname") String dname,
                                  @FormParam("x") String x, @FormParam("y") String y, @FormParam("c") String c, @FormParam("s1") String s1);

    @POST
    @Path("/build.php")
    Response confirmSendTroops(@QueryParam("gid") String gid, @QueryParam("tt") String tt,
                               @FormParam("redeployHero") String redeployHero, @FormParam("timestamp") String timestamp, @FormParam("timestamp_checksum") String checksum,
                               @FormParam("id") String id, @FormParam("a") String a, @FormParam("c") String c, @FormParam("kid") String kid,
                               @FormParam("t1") String t1, @FormParam("t2") String t2, @FormParam("t3") String t3, @FormParam("t4") String t4,
                               @FormParam("t5") String t5, @FormParam("t6") String t6, @FormParam("t7") String t7, @FormParam("t8") String t8,
                               @FormParam("t9") String t9, @FormParam("t10") String t10, @FormParam("t11") String t11, @FormParam("sendReally") String sendReally,
                               @FormParam("troopsSent") String troopsSent, @FormParam("currentDid") String currentDid, @FormParam("b") String b, @FormParam("dname") String dname,
                               @FormParam("x") String x, @FormParam("y") String y, @FormParam("s1") String s1);

    @GET
    @Path("/build.php")
    String getTroopsVillageActivity(@QueryParam("tt") String tt, @QueryParam("id") String id);


    @GET
    @Path("/build.php")
    Response getCancelTroops(@QueryParam("gid") String gid, @QueryParam("tt") String tt, @QueryParam("a") String a, @QueryParam("t") String t);

    @GET
    @Path("/build.php")
    String getWithdrawFromVillageView(@QueryParam("gid") String gid, @QueryParam("tt") String tt, @QueryParam("d") String d);

    @POST
    @Path("/build.php")
    Response withdrawFromVillage(@QueryParam("gid") String gid, @QueryParam("tt") String tt, @FormParam("a") String a, @FormParam("d") String d,
                                 @FormParam("t[1]") String t1, @FormParam("t[2]") String t2, @FormParam("t[3]") String t3,
                                 @FormParam("t[4]") String t4, @FormParam("s1") String s1);
}
