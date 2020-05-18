package helpers;

import merchant.preparemarketplace.merchantjson.PrepareMerchantJSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import restutils.AllRequestsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllStrings {

    public static final String PATH = "https://tx3.anglosphere.travian.com";
    public static final String EMAIL = "a";
    public static final String LOGIN = "b";
    public static final String PASSWORD = "c";
    public static final Map<Integer, String> BUILDING_ID_TO_BUILDING_NAME = new HashMap<>();
    public static final Map<String, String> UNIT_NAME_TO_REQUEST_PARAM = new HashMap<>();
    public static final List<String> EMPTY_FIELDS_LIST = new ArrayList<>();
    public static final Map<String, String> SOLDIER_TO_BUILDING_TRAIN = new HashMap<>();
    public static final String WAREHOUSE = "Warehouse";
    public static final String GRANARY = "Granary";
    public static final String RALLY_POINT = "Rally Point";
    public static final String MARKETPLACE = "Marketplace";
    public static final String BARRACKS = "Barracks";
    public static final String LUMBER = "Lumber";
    public static final String CLAY = "Clay";
    public static final String IRON = "Iron";
    public static final String CROP = "Crop";
    public static final String FREE_CROP = "Free crop";
    public static final String COOKIE = "Cookie";
    public static final String SOLDIER_PHALANX = "Phalanx";
    public static final String SOLDIER_SWORDSMAN = "Swordsman";
    public static final String SOLDIER_PATHFINDER = "Pathfinder";
    public static final String SOLDIER_THEUTATES_THUNDER = "Theutates Thunder";
    public static final String SOLDIER_DRUIDRIDER = "Druidrider";
    public static final String SOLDIER_HAEDUAN = "Haeduan";
    public static final String SOLDIER_RAM = "Ram";
    public static final String SOLDIER_TREBUCHET = "Trebuchet";
    public static final String SOLDIER_CHIEFTAIN = "Chieftain";
    public static final String SOLDIER_SETTLER = "Settler";
    public static final String SOLDIER_HERO = "Hero";
    public static final String T_SOLDIER_PHALANX = "t1";
    public static final String T_SOLDIER_SWORDSMAN = "t2";
    public static final String T_SOLDIER_PATHFINDER = "t3";
    public static final String T_SOLDIER_THEUTATES_THUNDER = "t4";
    public static final String T_SOLDIER_DRUIDRIDER = "t5";
    public static final String T_SOLDIER_HAEDUAN = "t6";
    public static final String T_SOLDIER_RAM = "t7";
    public static final String T_SOLDIER_TREBUCHET = "t8";
    public static final String T_SOLDIER_CHIEFTAIN = "t9";
    public static final String T_SOLDIER_SETTLER = "t10";
    public static final String T_SOLDIER_HERO = "t11";
    public static final String RESOURCES_WAREHOUSE_HTML = "stockBarWarehouse";
    public static final String RESOURCES_LUMBER_HTML = "l1";
    public static final String RESOURCES_CLAY_HTML = "l2";
    public static final String RESOURCES_IRON_HTML = "l3";
    public static final String RESOURCES_CROP_HTML = "l4";
    public static final String RESOURCES_GRANARY_HTML = "stockBarGranary";
    public static final String TIMER_HTML = "timer";
    public static final String NAME_HTML = "name";
    public static final String VALUE_HTML = "value";
    public static final String LVL_HTML = "lvl";
    public static final String DIV_BUILD_DURATION_HTML = "div.buildDuration";
    public static final String PRODUCTION_HTML = "production";
    public static final String TD_NUM_HTML = "td.num";
    public static final String TRAIN_UNITS_HTML = "trainUnits";
    public static final String INPUT_NAME_QUERY_HTML = "input[name='z']";
    public static final String TITLE_HTML = "tit";
    public static final String FURTHER_INFO_HTML = "furtherInfo";
    public static final String A_HTML = "a";
    public static final String OUT_ATTACK_HTML = "outAttack";
    public static final String TROOP_HEADLINE_HTML = "troopHeadline";
    public static final String RESOURCES_REQUIRED_LUMBER_HTML = "r1";
    public static final String RESOURCES_REQUIRED_CLAY_HTML = "r2";
    public static final String RESOURCES_REQUIRED_IRON_HTML = "r3";
    public static final String RESOURCES_REQUIRED_CROP_HTML = "r4";
    public static final String RESOURCES_REQUIRED_FREE_CROP_HTML = "r5";
    public static final String TD_DURATION_HTML = "td.dur";
    public static final String G_HTML = "g";
    public static final String CLASS_HTML = "class";
    public static final String MAP_HTML = "map";
    public static final String AREA_HTML = "area";
    public static final String HREF_HTML = "href";
    public static final String ALT_HTML = "alt";
    public static final String BUILD_HTML = "build";
    public static final String UNITS_HTML = "units";
    public static final String DIV_VILLAGE_MAP_HTML = "div#village_map";
    public static final String BUILDING_SLOT_HTML = "buildingSlot";
    public static final String CLICK_SHAPE_HTML = "clickShape";
    public static final String ONCLICK_HTML = "onclick";
    public static final String BUTTON_HTML = "button";
    public static final String ABORT_HTML = "abort";
    public static final String DIV_SECTION1_HTML = "div.section1";
    public static final String DIV_CONTRACT_LINK_HTML = "div.contractLink";
    public static final String LABEL_LAYER_HTML = "labelLayer";
    public static final String BUILDING_WRAPPER_HTML = "buildingWrapper";
    public static final String BUILDING_LIST_SOON_HTML = "build_list_soon";
    public static final String H2_HTML = "h2";
    public static final String AJAX_PARAM_HTML = "Travian.Game.country";
    public static final String MERCHANTS_AVAILABLE_HTML = "merchantsAvailable";
    public static final String TH_HTML = "th";
    public static final String TR_HTML = "tr";
    public static final String TD_HTML = "td";
    public static final String SZ_PARAM = "sz";
    public static final String KID_PARAM = "kid";
    public static final String C_HTML = "c";
    public static final String MOVEMENTS_HTML = "movements";
    public static final String INCOMING_HTML = "Incoming";
    public static final String TD_UNIT = "td.unit";
    public static final String ATTACK_REINFORCEMENT = "Reinforcement";
    public static final String ATTACK_NORMAL = "Attack: Normal";
    public static final String ATTACK_RAID = "Attack: Raid";
    public static final String SND_HTML = "snd";
    public static final String TIMESTAMP_HTML = "timestamp";
    public static final String CHECKSUM_HTML = "timestamp_checksum";
    public static final String CURRENT_DID_HTML = "currentDid";
    public static final String REDEPLOY_HERO_HTML = "redeployHero";
    public static final String ID_HTML = "id";
    public static final String IN_HTML = "in";
    public static final String SEND_REALLY_PARAM = "sendReally";
    public static final String TROOPS_SENT_PARAM = "troopsSent";
    public static final String FINISH_NOW_HTML = "finishNow";
    public static final String B_HTML = "b";
    public static final String D_NAME_HTML = "dname";
    public static final String X_PARAM = "x";
    public static final String Y_PARAM = "y";
    public static final String S1_PARAM = "s1";
    public static final String PREPARE_MARKETPLACE_PARAM = "prepareMarketplace";
    public static final String ERROR_HTML = "error";
    public static final String STABLE = "Stable";
    public static final String VILLAGE_NAME_FIELD_HTML = "villageNameField";
    public static final String FORM_HTML = "form";
    public static final String TROOPS_IN_VILLAGE_HTML = "Troops in this village";
    static final String SET_COOKIE = "Set-Cookie";
    private static final Logger LOGGER = LogManager.getLogger(AllStrings.class);
    private static final String EMPTY_FIELD = "Empty field";
    private static final String SAWMILL = "Sawmill";
    private static final String BRICKYARD = "Brickyard";
    private static final String IRON_FOUNDRY = "Iron Foundry";
    private static final String GRAIN_MILL = "Grain Mill";
    private static final String BAKERY = "Bakery";
    private static final String SMITHY = "Smithy";
    private static final String TOURNAMENT_SQUARE = "Tournament Square";
    private static final String MAIN_BUILDING = "Main Building";
    private static final String EMBASSY = "Embassy";
    private static final String WORKSHOP = "Workshop";
    private static final String ACADEMY = "Academy";
    private static final String CRANNY = "Cranny";
    private static final String TOWN_HALL = "Town Hall";
    private static final String RESIDENCE = "Residence";
    private static final String TREASURY = "Treasury";
    private static final String TRADE_OFFICE = "Trade Office";
    private static final String PALISADE = "Palisade";
    private static final String STONEMASON_S_LODGE = "Stonemason's Lodge";
    private static final String TRAPPER = "Trapper";
    private static final String HERO_S_MANSION = "Hero's Mansion";
    private static final String GREAT_WAREHOUSE = "Great Warehouse";
    private static final String GREAT_GRANARY = "Great Granary";
    public static String cookie = "";
    public static int TIME_TO_FINISH_BUILD = 0;
    public static int TIME_TO_FINISH_TRAINING = 0;
    public static int TIME_TO_ARRIVE_ATTACK = 0;
    public static String CURRENT_TASK_BUILD = "";
    public static String CURRENT_TROOP_TRAINING = "";
    public static String CURRENT_ATTACK_OUTGOING = "";
    public static String BARRACKS_FIELD = "";
    public static String MARKETPLACE_FIELD = "";
    public static String RALLY_POINT_FIELD = "";
    public static String STABLE_FIELD = "";
    public static String Z_PARAM = "";
    public static String C_PARAM = "";
    public static String A_PARAM = "";
    public static String AJAX_PARAM = "";
    public static PrepareMerchantJSON LAST_MERCHANT_JSON;
    public static String AVAILABLE_MERCHANTS;
    public static String CURRENT_VILLAGE_NAME = "";

    static {
        LOGGER.info("Initializing maps in AllStrings");
        BUILDING_ID_TO_BUILDING_NAME.put(0, EMPTY_FIELD);
        BUILDING_ID_TO_BUILDING_NAME.put(5, SAWMILL);
        BUILDING_ID_TO_BUILDING_NAME.put(6, BRICKYARD);
        BUILDING_ID_TO_BUILDING_NAME.put(7, IRON_FOUNDRY);
        BUILDING_ID_TO_BUILDING_NAME.put(8, GRAIN_MILL);
        BUILDING_ID_TO_BUILDING_NAME.put(9, BAKERY);
        BUILDING_ID_TO_BUILDING_NAME.put(10, WAREHOUSE);
        BUILDING_ID_TO_BUILDING_NAME.put(11, GRANARY);
        BUILDING_ID_TO_BUILDING_NAME.put(12, "");
        BUILDING_ID_TO_BUILDING_NAME.put(13, SMITHY);
        BUILDING_ID_TO_BUILDING_NAME.put(14, TOURNAMENT_SQUARE);
        BUILDING_ID_TO_BUILDING_NAME.put(15, MAIN_BUILDING);
        BUILDING_ID_TO_BUILDING_NAME.put(16, RALLY_POINT);
        BUILDING_ID_TO_BUILDING_NAME.put(17, MARKETPLACE);
        BUILDING_ID_TO_BUILDING_NAME.put(18, EMBASSY);
        BUILDING_ID_TO_BUILDING_NAME.put(19, BARRACKS);
        BUILDING_ID_TO_BUILDING_NAME.put(20, STABLE);
        BUILDING_ID_TO_BUILDING_NAME.put(21, WORKSHOP);
        BUILDING_ID_TO_BUILDING_NAME.put(22, ACADEMY);
        BUILDING_ID_TO_BUILDING_NAME.put(23, CRANNY);
        BUILDING_ID_TO_BUILDING_NAME.put(24, TOWN_HALL);
        BUILDING_ID_TO_BUILDING_NAME.put(25, RESIDENCE);
        BUILDING_ID_TO_BUILDING_NAME.put(26, "");
        BUILDING_ID_TO_BUILDING_NAME.put(27, TREASURY);
        BUILDING_ID_TO_BUILDING_NAME.put(28, TRADE_OFFICE);
        BUILDING_ID_TO_BUILDING_NAME.put(29, "");
        BUILDING_ID_TO_BUILDING_NAME.put(30, "");
        BUILDING_ID_TO_BUILDING_NAME.put(31, "");
        BUILDING_ID_TO_BUILDING_NAME.put(32, "");
        BUILDING_ID_TO_BUILDING_NAME.put(33, PALISADE);
        BUILDING_ID_TO_BUILDING_NAME.put(34, STONEMASON_S_LODGE);
        BUILDING_ID_TO_BUILDING_NAME.put(35, "");
        BUILDING_ID_TO_BUILDING_NAME.put(36, TRAPPER);
        BUILDING_ID_TO_BUILDING_NAME.put(37, HERO_S_MANSION);
        BUILDING_ID_TO_BUILDING_NAME.put(38, GREAT_WAREHOUSE);
        BUILDING_ID_TO_BUILDING_NAME.put(39, GREAT_GRANARY);

        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_PHALANX, T_SOLDIER_PHALANX);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_SWORDSMAN, T_SOLDIER_SWORDSMAN);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_PATHFINDER, T_SOLDIER_PATHFINDER);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_THEUTATES_THUNDER, T_SOLDIER_THEUTATES_THUNDER);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_DRUIDRIDER, T_SOLDIER_DRUIDRIDER);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_HAEDUAN, T_SOLDIER_HAEDUAN);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_RAM, T_SOLDIER_RAM);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_TREBUCHET, T_SOLDIER_TREBUCHET);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_CHIEFTAIN, T_SOLDIER_CHIEFTAIN);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_SETTLER, T_SOLDIER_SETTLER);
        UNIT_NAME_TO_REQUEST_PARAM.put(SOLDIER_HERO, T_SOLDIER_HERO);
    }

    public static void setSoldierToBuildingTrain() {
        SOLDIER_TO_BUILDING_TRAIN.put(T_SOLDIER_PHALANX, BARRACKS_FIELD);
        SOLDIER_TO_BUILDING_TRAIN.put(T_SOLDIER_SWORDSMAN, BARRACKS_FIELD);
        SOLDIER_TO_BUILDING_TRAIN.put(T_SOLDIER_PATHFINDER, STABLE_FIELD);
        SOLDIER_TO_BUILDING_TRAIN.put(T_SOLDIER_THEUTATES_THUNDER, STABLE_FIELD);
    }

    public static void setMarketplaceField(String marketplaceField) {
        MARKETPLACE_FIELD = AllRequestsHelper.findTypeOfBuildingId(marketplaceField);
    }

    public static void setBarracksField(String barracksField) {
        BARRACKS_FIELD = AllRequestsHelper.findTypeOfBuildingId(barracksField);
    }

    public static void setRallyPointField(String rallyPointField) {
        RALLY_POINT_FIELD = AllRequestsHelper.findTypeOfBuildingId(rallyPointField);
    }

    public static void setStableField(String stableField) {
        STABLE_FIELD = AllRequestsHelper.findTypeOfBuildingId(stableField);
    }

    public static List<String> getEmptyFieldsList() {
        if (EMPTY_FIELDS_LIST.isEmpty()) {
            AllRequestsHelper.getVillageBuildingsList();
        }
        return EMPTY_FIELDS_LIST;
    }
}
