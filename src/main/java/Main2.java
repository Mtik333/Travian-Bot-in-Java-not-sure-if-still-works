import helpers.AllStrings;
import helpers.VariousHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import restutils.AllRequestsHelper;
import restutils.ClientFactory;

import javax.ws.rs.core.Response;

class Main2 {

    private static final Logger LOGGER = LogManager.getLogger(Main2.class);

    public static void main(String[] args) {
        ClientFactory clientFactory = new ClientFactory();
        //test LOGIN
        //LOGGER.info("Entering class Main2");
        Response response = clientFactory.getLoginClient().loginToTravian(AllStrings.LOGIN, AllStrings.PASSWORD);
        VariousHelper.setCookie(response);
//        String response1 = clientFactory.getLoginClient().getResources();
//        System.out.println(response1);
//        //test map resources
//        String myResourceId = AllRequestsHelper.villageResourcesList(response1);
//        String response2 = clientFactory.getResourceClient().getResourceInfo(myResourceId);
//        System.out.println(response2);
//        //upgrade resource
//        String[] params = AllRequestsHelper.getParamsToUpgradeResource(response2);
//        String response3 = clientFactory.getResourceClient().getUpgradeResource(params);
//        System.out.println(response3);
//        //test message sending
//        Response response4 = clientFactory.getMessageClient().postMessageResponse(AllStrings.LOGIN, "something", "wow");
//        System.out.println(response4);

        AllRequestsHelper.getWithdrawArmy();
        //AllRequestsHelper.ajaxParam();
        //AllRequestsHelper.cancelMilitaryActivity();

        //test building map
        //String response5 = clientFactory.getLoginClient().getBuildings();
        //String test = AllRequestsHelper.villageBuildingsList(response5);

        //test timer
        //AllRequestsHelper.timeToFinishBuilding(response5);
        //AllRequestsHelper.getAvailableBuildingToBuild(clientFactory.getBuildingClient().getNewBuildingCategory("37","1"));
        //AllRequestsHelper.productionPerHour(clientFactory.getLoginClient().getResources());

        //test new units
        //String response6 = clientFactory.getMilitaryClient().getBarracksBuilding("36");
        //AllRequestsHelper.getPossibleUnitsToTrain(response6);

        //test build new building
        //clientFactory.

        //merchant send resources: build.php?t=5&id=33
        //ajax.php?cmd=prepareMarketplace
//        cmd	prepareMarketplace
//        r1	1  lumber
//        r2	1  clay
//        r3	1  iron
//        r4	1  crop
//        dname
//        x	-16
//        y	100
//        id	33  marketplace building
//        t	5
//        x2	1  merchant num
//        ajaxToken	a49f6fb87ccb3e2079b2a4b9355d28c4

        //Travian.thematicallyRetakingSeized = function() {
        //        return 'a49f6fb87ccb3e2079b2a4b9355d28c4';
        //    }


        //send troops

        //od t1 do t10 - troops
        //x
        //y
        //c (1-reinforcement, 3 - attack normal, 4 - raid)
        //timestamp
        //timestamp_checksum
        //b
        //currentDid
        //gid (16)?


    }
}
