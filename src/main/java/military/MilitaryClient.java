package military;

import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import restutils.AbstractClient;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;

public class MilitaryClient extends AbstractClient<MilitaryService> {

    private static final Logger LOGGER = LogManager.getLogger(MilitaryClient.class);

    public MilitaryClient(ResteasyWebTarget stub) {
        super(stub, MilitaryService.class);
    }

    public String getBarracksBuilding(String field) {
        LOGGER.info("Entering getBarracksBuilding");
        return execute(() -> service.getBarracksBuilding(field));
    }

    public Response postTrainTroops(String type, String num, String z) {
        LOGGER.info("Entering postTrainTroops: type: " + type + ", num: " + num + ", z: " + z);
        String buildingField = AllStrings.SOLDIER_TO_BUILDING_TRAIN.get(type);
        switch (type) {
            case AllStrings.T_SOLDIER_PHALANX:
                return execute(() -> service.trainTroops(buildingField, z, "2", "1", num, "", "ok"));
            case AllStrings.T_SOLDIER_SWORDSMAN:
                return execute(() -> service.trainTroops(buildingField, z, "2", "1", "", num, "ok"));
            case AllStrings.T_SOLDIER_PATHFINDER:
                return execute(() -> service.trainTroopsStable(buildingField, z, "2", "1", num,"", "ok"));
            case AllStrings.T_SOLDIER_THEUTATES_THUNDER:
                return execute(() -> service.trainTroopsStable(buildingField, z, "2", "1", "", num, "ok"));
            default:
                return null;
        }
    }

    public String prepareTroopsForAttack(String timestamp, String checksum, String currentDid, String t1, String t2, String t3, String t4, String t5, String t6, String t7, String t8, String t9, String t10, String t11, String x, String y, String c) {
        LOGGER.info("Entering prepareTroopsForAttack");
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        return execute(() -> service.prepareTroopsForAttack("16", "2", timestamp, checksum, "1", currentDid, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, "", x, y, c, "ok"));
    }

    public Response confirmSendTroops(String redeployHero, String timestamp, String checksum,
                                      String id, String a, String c, String kid,
                                      String t1, String t2, String t3, String t4,
                                      String t5, String t6, String t7, String t8,
                                      String t9, String t10, String t11, String sendReally,
                                      String troopsSent, String currentDid, String b, String dname,
                                      String x, String y, String s1) {
        LOGGER.info("Entering confirmSendTroops");
        return execute(() -> service.confirmSendTroops("16", "2", redeployHero, timestamp, checksum, id, a, c, kid, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, sendReally, troopsSent, currentDid, b, dname, x, y, s1));
    }

    public String getTroopsVillageActivity(String tt) {
        LOGGER.info("Entering getTroopsVillageActivity: tt: " + tt);
        if (AllStrings.RALLY_POINT_FIELD.isEmpty()) {
            AllStrings.setRallyPointField(AllStrings.RALLY_POINT);
        }
        if (AllStrings.BARRACKS_FIELD.isEmpty()) {
            AllStrings.setBarracksField(AllStrings.BARRACKS);
        }
        if (AllStrings.STABLE_FIELD.isEmpty()) {
            AllStrings.setStableField(AllStrings.STABLE);
        }
        AllStrings.setSoldierToBuildingTrain();
        return execute(() -> service.getTroopsVillageActivity(tt, AllStrings.RALLY_POINT_FIELD));
    }

    public Response getCancelTroops(String tt, String a, String t) {
        LOGGER.info("Entering getCancelTroops");
        return execute(() -> service.getCancelTroops("16", tt, a, t));
    }

    public String getWithdrawArmyView(String tt, String d){
        LOGGER.info("Entering getWithdrawArmyView");
        return execute(() -> service.getWithdrawFromVillageView("16",tt,d));
    }

    public Response withdrawArmyFromVillage(String tt, String a, String d, String t1, String t2, String t3, String t4){
        LOGGER.info("Entering withdrawArmyFromVillage");
        return execute(() -> service.withdrawFromVillage("16", tt, a, d, t1, t2,t3,t4,"ok"));
    }
}
