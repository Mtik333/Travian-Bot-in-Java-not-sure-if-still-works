package restutils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.AllStrings;
import helpers.VariousHelper;
import merchant.autocomplete.merchantjson.VillageNameMerchantJSON;
import merchant.preparemarketplace.merchantjson.PrepareMerchantJSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AllRequestsHelper {

    private static final Logger LOGGER = LogManager.getLogger(AllRequestsHelper.class);

    private static ClientFactory clientFactory;

    public static ClientFactory getClientFactory() {
        LOGGER.info("Entering getClientFactory");
        if (clientFactory == null) {
            clientFactory = new ClientFactory();
            Response response = clientFactory.getLoginClient().loginToTravian(AllStrings.LOGIN, AllStrings.PASSWORD);
            VariousHelper.setCookie(response);
        }
        return clientFactory;
    }

    public static Map<String, String> getCurrentResources() {
        LOGGER.info("Entering getCurrentResources");
        String response = getClientFactory().getLoginClient().getResources();
        Document html = Jsoup.parse(response);
        setVillageName(html);
        Map<String, String> resourceMap = new HashMap<>();
        resourceMap.put(AllStrings.WAREHOUSE, getSpecificCurrentResourceValue(AllStrings.RESOURCES_WAREHOUSE_HTML, html));
        resourceMap.put(AllStrings.LUMBER, getSpecificCurrentResourceValue(AllStrings.RESOURCES_LUMBER_HTML, html));
        resourceMap.put(AllStrings.CLAY, getSpecificCurrentResourceValue(AllStrings.RESOURCES_CLAY_HTML, html));
        resourceMap.put(AllStrings.IRON, getSpecificCurrentResourceValue(AllStrings.RESOURCES_IRON_HTML, html));
        resourceMap.put(AllStrings.GRANARY, getSpecificCurrentResourceValue(AllStrings.RESOURCES_GRANARY_HTML, html));
        resourceMap.put(AllStrings.CROP, getSpecificCurrentResourceValue(AllStrings.RESOURCES_CROP_HTML, html));
        return resourceMap;
    }

    public static boolean postSendMessage(String username, String subject, String message) {
        LOGGER.info("Entering postSendMessage: username: " + username + ", subject: " + subject + ", message: " + message);
        Response response = getClientFactory().getMessageClient().postMessageResponse(username, subject, message);
        return response.getStatus() == 302;
    }

    public static int getTimeToFinishBuilding() {
        LOGGER.info("Entering getTimeToFinishBuilding");
        String response = getClientFactory().getLoginClient().getResources();
        Document fullHtml = Jsoup.parse(response);
        Elements divElements = fullHtml.select(AllStrings.DIV_BUILD_DURATION_HTML);
        if (!divElements.isEmpty()) {
            AllStrings.TIME_TO_FINISH_BUILD = Integer.parseInt(divElements.first().getElementsByClass(AllStrings.TIMER_HTML).first().attributes().get(AllStrings.VALUE_HTML));
            String building = divElements.prev().first().html();
            building = building.substring(0, building.indexOf("\n")).trim();
            String lvl = divElements.prev().first().getElementsByClass(AllStrings.LVL_HTML).html();
            AllStrings.CURRENT_TASK_BUILD = building + ":" + lvl;
        } else {
            AllStrings.TIME_TO_FINISH_BUILD = 0;
            AllStrings.CURRENT_TASK_BUILD = "";
        }
        return AllStrings.TIME_TO_FINISH_BUILD;
    }

    public static Map<String, String> getProductionPerHour() {
        LOGGER.info("Entering getProductionPerHour");
        String response = getClientFactory().getLoginClient().getResources();
        Document fullHtml = Jsoup.parse(response);
        Map<String, String> productionInHours = new HashMap<>();
        Element tableElements = fullHtml.getElementById(AllStrings.PRODUCTION_HTML);
        if (tableElements != null) {
            Elements productionValues = tableElements.select(AllStrings.TD_NUM_HTML);
            productionInHours.put(AllStrings.LUMBER, productionValues.get(0).html());
            productionInHours.put(AllStrings.CLAY, productionValues.get(1).html());
            productionInHours.put(AllStrings.IRON, productionValues.get(2).html());
            productionInHours.put(AllStrings.CROP, productionValues.get(3).html());
        }
        return productionInHours;
    }

    public static Map<String, String> getPossibleUnitsToTrain() {
        LOGGER.info("Entering getPossibleUnitsToTrain");
        Map<String, String> unitsToTrain = new HashMap<>();
        if (!AllStrings.BARRACKS_FIELD.isEmpty())
            unitsToTrain.putAll(getAllUnitsToTrain(AllStrings.BARRACKS_FIELD));
        if (!AllStrings.STABLE_FIELD.isEmpty())
            unitsToTrain.putAll(getAllUnitsToTrain(AllStrings.STABLE_FIELD));
        return unitsToTrain;
    }

    private static Map<String, String> getAllUnitsToTrain(String field) {
        LOGGER.info("Entering getAllUnitsToTrain: field: " + field);
        String response = getClientFactory().getMilitaryClient().getBarracksBuilding(field);
        Document fullHtml = Jsoup.parse(response);
        Map<String, String> unitsToTrain = new HashMap<>();
        Elements elements = fullHtml.getElementsByClass(AllStrings.TRAIN_UNITS_HTML);
        AllStrings.Z_PARAM = fullHtml.select(AllStrings.INPUT_NAME_QUERY_HTML).val();
        Elements unitElements = elements.first().getElementsByClass(AllStrings.TITLE_HTML);
        for (Element unitElement : unitElements) {
            String unitName = unitElement.getElementsByClass(AllStrings.FURTHER_INFO_HTML).prev().html();
            String maxUnits = unitElement.parent().select(AllStrings.A_HTML).last().html();
            unitsToTrain.put(unitName, maxUnits);
        }
        return unitsToTrain;
    }

    public static Map<String, String> getResourcesRequiredForUnit(String unit) {
        LOGGER.info("Entering getResourcesRequiredForUnit: unit: " + unit);
        String response = getClientFactory().getMilitaryClient().getBarracksBuilding(AllStrings.BARRACKS_FIELD);
        Document fullHtml = Jsoup.parse(response);
        Elements troopElements = fullHtml.getElementsContainingOwnText(unit);
        Element troopElement;
        if (troopElements.isEmpty()) {
            response = getClientFactory().getMilitaryClient().getBarracksBuilding(AllStrings.STABLE_FIELD);
            fullHtml = Jsoup.parse(response);
            troopElement = fullHtml.getElementsContainingOwnText(unit).first().parent().parent();
            return getResourceRequirements(troopElement);
        } else troopElement = troopElements.first().parent().parent();
        return getResourceRequirements(troopElement);
    }

    private static Map<String, String> getResourceRequirements(Element html) {
        LOGGER.info("Entering getResourceRequirements");
        Map<String, String> resourcesRequired = new HashMap<>();
        resourcesRequired.put(AllStrings.LUMBER, getSpecificRequiredResourceValue(AllStrings.RESOURCES_REQUIRED_LUMBER_HTML, html));
        resourcesRequired.put(AllStrings.CLAY, getSpecificRequiredResourceValue(AllStrings.RESOURCES_REQUIRED_CLAY_HTML, html));
        resourcesRequired.put(AllStrings.IRON, getSpecificRequiredResourceValue(AllStrings.RESOURCES_REQUIRED_IRON_HTML, html));
        resourcesRequired.put(AllStrings.CROP, getSpecificRequiredResourceValue(AllStrings.RESOURCES_REQUIRED_CROP_HTML, html));
        resourcesRequired.put(AllStrings.FREE_CROP, getSpecificRequiredResourceValue(AllStrings.RESOURCES_REQUIRED_FREE_CROP_HTML, html));
        return resourcesRequired;
    }

    private static String getSpecificRequiredResourceValue(String resClass, Element html) {
        LOGGER.info("Entering getSpecificRequiredResourceValue: resClass: " + resClass);
        Elements elements = html.getElementsByClass(resClass);
        if (!elements.isEmpty()) {
            return elements.get(0).getElementsByClass(AllStrings.VALUE_HTML).html().replace(",", "");
        } else return "";
    }

    private static String getSpecificCurrentResourceValue(String resId, Element html) {
        LOGGER.info("Entering getSpecificCurrentResourceValue: resId: " + resId);
        Element element = html.getElementById(resId);
        if (element != null) {
            return element.html().replace(",", "");
        } else return "";
    }

    public static boolean postTrainSoldiers(String type, String number) {
        LOGGER.info("Entering postTrainSoldiers: type: " + type + ", number: " + number);
        String reqParam = AllStrings.UNIT_NAME_TO_REQUEST_PARAM.get(type);
        Response response = getClientFactory().getMilitaryClient().postTrainTroops(reqParam, number, AllStrings.Z_PARAM);
        return response.getStatus() == 200;
    }

    public static int getTimeToFinishTrain() {
        LOGGER.info("Entering getTimeToFinishTrain");
        String response = getClientFactory().getMilitaryClient().getBarracksBuilding(AllStrings.BARRACKS_FIELD);
        Document fullHtml = Jsoup.parse(response);
        Elements divElements = fullHtml.select(AllStrings.TD_DURATION_HTML);
        if (!divElements.isEmpty()) {
            AllStrings.TIME_TO_FINISH_TRAINING = Integer.parseInt(divElements.first().getElementsByClass(AllStrings.TIMER_HTML).first().attributes().get(AllStrings.VALUE_HTML));
            AllStrings.CURRENT_TROOP_TRAINING = divElements.prev().first().text();
            return AllStrings.TIME_TO_FINISH_TRAINING;
        } else {
            if (!AllStrings.STABLE_FIELD.isEmpty()){
                response = getClientFactory().getMilitaryClient().getBarracksBuilding(AllStrings.STABLE_FIELD);
                fullHtml = Jsoup.parse(response);
                divElements = fullHtml.select(AllStrings.TD_DURATION_HTML);
                if (!divElements.isEmpty()) {
                    AllStrings.TIME_TO_FINISH_TRAINING = Integer.parseInt(divElements.first().getElementsByClass(AllStrings.TIMER_HTML).first().attributes().get(AllStrings.VALUE_HTML));
                    AllStrings.CURRENT_TROOP_TRAINING = divElements.prev().first().text();
                    return AllStrings.TIME_TO_FINISH_TRAINING;
                }
            }
            AllStrings.CURRENT_TROOP_TRAINING = "";
            AllStrings.TIME_TO_FINISH_TRAINING = 0;
        }
        return AllStrings.TIME_TO_FINISH_TRAINING;
    }

    public static String findTypeOfBuildingId(String building) {
        LOGGER.info("Entering findTypeOfBuildingId: building: " + building);
        String response = getClientFactory().getLoginClient().getBuildings();
        Document fullHtml = Jsoup.parse(response);
        String buildingClassId = AllStrings.BUILDING_ID_TO_BUILDING_NAME.entrySet().stream().filter(elem -> elem.getValue().contentEquals(building)).findFirst().get().getKey().toString();
        Elements barracks = fullHtml.getElementsByClass(AllStrings.G_HTML + buildingClassId);
        if (!barracks.isEmpty()) {
            String[] classes = barracks.first().attributes().get(AllStrings.CLASS_HTML).split(" ");
            return classes[1].replace(AllStrings.A_HTML, "");
        } else return "";
    }

    public static Map<String, String> getVillageResourcesList() {
        LOGGER.info("Entering getVillageResourcesList");
        String html = getClientFactory().getLoginClient().getResources();
        Document fullHtml = Jsoup.parse(html);
        Elements resources = fullHtml.select(AllStrings.MAP_HTML);
        Element mapElement = resources.get(0);
        Elements buildResourceBuilding = mapElement.getElementsByTag(AllStrings.AREA_HTML);
        buildResourceBuilding.remove(buildResourceBuilding.last());
        Map<String, String> buildingDetails = new HashMap<>();
        for (Element element : buildResourceBuilding) {
            String resourceId = element.attributes().get(AllStrings.HREF_HTML);
            String id = resourceId.substring(resourceId.lastIndexOf("=") + 1);
            buildingDetails.put("Field " + id + "-" + element.attributes().get(AllStrings.ALT_HTML), id);
        }
        return buildingDetails;
    }

    public static Map<String, String> getResourcesToUpgradeResourceBuilding(String id) {
        LOGGER.info("Entering getResourcesToUpgradeResourceBuilding");
        String html = getClientFactory().getResourceClient().getResourceInfo(id);
        Document fullHtml = Jsoup.parse(html);
        Element upgradeElement = fullHtml.getElementById(AllStrings.BUILD_HTML);
        Map<String, String> resourcesToUpgrade = getResourceRequirements(upgradeElement);
        AllStrings.A_PARAM = id;
        AllStrings.C_PARAM = getParamsToUpgradeResource(upgradeElement, true);
        return resourcesToUpgrade;
    }

    public static String getUpgradeResourceBuilding() {
        LOGGER.info("Entering getUpgradeResourceBuilding");
        return getClientFactory().getResourceClient().getUpgradeResource(AllStrings.A_PARAM, AllStrings.C_PARAM);
    }

    public static Map<String, String> getVillageBuildingsList() {
        LOGGER.info("Entering getVillageBuildingsList");
        Map<String, String> buildingDetails = new HashMap<>();
        String html = getClientFactory().getLoginClient().getBuildings();
        AllStrings.setBarracksField(AllStrings.BARRACKS);
        AllStrings.setMarketplaceField(AllStrings.MARKETPLACE);
        Document fullHtml = Jsoup.parse(html);
        Elements resources = fullHtml.select(AllStrings.DIV_VILLAGE_MAP_HTML);
        Element villageMapElement = resources.get(0);
        Elements buildNormalBuilding = villageMapElement.getElementsByClass(AllStrings.BUILDING_SLOT_HTML);
        for (Element element : buildNormalBuilding) {
            if (element.getAllElements().size() != 1) {
                int level = getLevelOfBuilding(element);
                Element pathElem = element.getElementsByClass(AllStrings.CLICK_SHAPE_HTML).get(0).getAllElements().get(1);
                String resourceId = pathElem.attributes().get(AllStrings.ONCLICK_HTML);
                String buildingId = resourceId.substring(resourceId.lastIndexOf("=") + 1, resourceId.length() - 1);
                if (level == 0) {
                    if (!AllStrings.EMPTY_FIELDS_LIST.contains(buildingId))
                        AllStrings.EMPTY_FIELDS_LIST.add(buildingId);
                    continue;
                }
                String[] divClasses = element.attributes().get(AllStrings.CLASS_HTML).split(" ");
                buildingDetails.put(buildingId + "-" + getBuildingNameFromHTML(divClasses[2]) + " - Level " + level, buildingId);
            }
        }
        return buildingDetails;
    }

    private static String getParamsToUpgradeResource(Element fullHtml, boolean isUpgrade) {
        LOGGER.info("Entering getParamsToUpgradeResource: isUpgrade: " + isUpgrade);
        Elements divElements;
        if (isUpgrade)
            divElements = fullHtml.select(AllStrings.DIV_SECTION1_HTML);
        else divElements = fullHtml.select(AllStrings.DIV_CONTRACT_LINK_HTML);
        Element divElement = divElements.get(0);
        Elements buttons = divElement.getElementsByTag(AllStrings.BUTTON_HTML);
        try {
            String locationToUpgrade = buttons.first().attributes().get(AllStrings.ONCLICK_HTML);
            return locationToUpgrade.substring(locationToUpgrade.indexOf("c=") + 2, locationToUpgrade.lastIndexOf("'"));
        } catch (Exception exp) {
            return "";
        }
    }

    private static int getLevelOfBuilding(Element html) {
        LOGGER.info("Entering getLevelOfBuilding");
        Elements divElements = html.getElementsByClass(AllStrings.LABEL_LAYER_HTML);
        if (!divElements.isEmpty()) {
            return Integer.parseInt(divElements.first().html());
        } else return 0;
    }

    private static String getBuildingNameFromHTML(String htmlClass) {
        LOGGER.info("Entering getBuildingNameFromHTML: htmlClass: " + htmlClass);
        Integer buildingInt = Integer.parseInt(StringUtils.getDigits(htmlClass));
        List<String> building = AllStrings.BUILDING_ID_TO_BUILDING_NAME.entrySet().stream().filter(entry -> entry.getKey().equals(buildingInt)).map(Map.Entry::getValue).collect(Collectors.toList());
        if (!building.isEmpty())
            return building.get(0);
        else return "";
    }

    public static List<String> getAvailableBuildingToBuild() {
        LOGGER.info("Entering getAvailableBuildingToBuild");
        List<String> buildings = new ArrayList<>();
        if (AllStrings.EMPTY_FIELDS_LIST.isEmpty()) {
            getVillageBuildingsList();
        }
        String firstEmptyField = AllStrings.EMPTY_FIELDS_LIST.get(0);
        buildings.addAll(getAvailableBuildingsByCategory(firstEmptyField, "1"));
        buildings.addAll(getAvailableBuildingsByCategory(firstEmptyField, "2"));
        buildings.addAll(getAvailableBuildingsByCategory(firstEmptyField, "3"));
        return buildings;
    }

    private static List<String> getAvailableBuildingsByCategory(String firstEmptyField, String category) {
        LOGGER.info("Entering getAvailableBuildingsByCategory: firstEmptyField: " + firstEmptyField + ", category: " + category);
        String response = getClientFactory().getBuildingClient().getNewBuildingCategory(firstEmptyField, category);
        Document html = Jsoup.parse(response);
        List<String> buildings = new ArrayList<>();
        Elements availableBuildings = html.getElementsByClass(AllStrings.BUILDING_WRAPPER_HTML);
        for (Element building : availableBuildings) {
            if (!building.parent().id().contentEquals(AllStrings.BUILDING_LIST_SOON_HTML)) {
                buildings.add(building.getElementsByTag(AllStrings.H2_HTML).html());
            }
        }
        return buildings;
    }

    public static String ajaxParam() {
        LOGGER.info("Entering ajaxParam");
        String response = getClientFactory().getLoginClient().getBuildings();
        String relatedToAjaxParam = response.substring(response.indexOf(AllStrings.AJAX_PARAM_HTML) + 409, response.indexOf(AllStrings.AJAX_PARAM_HTML) + 535);
        AllStrings.AJAX_PARAM = relatedToAjaxParam.substring(relatedToAjaxParam.indexOf("'") + 1, relatedToAjaxParam.lastIndexOf("'"));
        return AllStrings.AJAX_PARAM;
    }

    public static boolean prepareMerchantResponse(String lumber, String clay, String iron, String crop, String x, String y, String dname) {
        LOGGER.info("Entering prepareMerchantResponse: lumber: " + lumber + ", clay: " + clay + ", crop: " + crop + ", x: " + x + ", y: " + y);
        Response response = getClientFactory().getMerchantClient().postPrepareMarketplace(lumber, clay, iron, crop, x, y, dname);
        String jsonResponse = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            PrepareMerchantJSON prepareMerchantJSON = mapper.readValue(jsonResponse, PrepareMerchantJSON.class);
            AllStrings.LAST_MERCHANT_JSON = prepareMerchantJSON;
            if (prepareMerchantJSON.getResponse().getData().getErrorMessage().isEmpty()) {
                getRequiredParamsForMerchant(prepareMerchantJSON);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static Map<String, String> getInfoAboutMerchantForm() {
        LOGGER.info("Entering getInfoAboutMerchantForm");
        Map<String, String> formInfo = new HashMap<>();
        String responseMarketplace = getClientFactory().getMerchantClient().getMarketplaceHtmlSendResources();
        Document htmlDoc = Jsoup.parse(responseMarketplace);
        Document dataDoc = Jsoup.parse(AllStrings.LAST_MERCHANT_JSON.getResponse().getData().getFormular());
        AllStrings.AVAILABLE_MERCHANTS = htmlDoc.getElementsByClass(AllStrings.MERCHANTS_AVAILABLE_HTML).first().html();
        Elements tableElements = dataDoc.select(AllStrings.TR_HTML);
        for (Element element : tableElements) {
            String header = element.select(AllStrings.TH_HTML).html();
            String value = element.select(AllStrings.TD_HTML).text();
            formInfo.put(header, value);
        }
        return formInfo;
    }

    private static Map<String, String> getRequiredParamsForMerchant(PrepareMerchantJSON prepareMerchantJSON) {
        LOGGER.info("Entering getRequiredParamsForMerchant");
        Document elements = Jsoup.parse(prepareMerchantJSON.getResponse().getData().getFormular());
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(AllStrings.A_HTML, elements.getElementById(AllStrings.A_HTML).val());
        paramsMap.put(AllStrings.SZ_PARAM, elements.getElementById(AllStrings.SZ_PARAM).val());
        paramsMap.put(AllStrings.KID_PARAM, elements.getElementById(AllStrings.KID_PARAM).val());
        paramsMap.put(AllStrings.C_HTML, elements.getElementById(AllStrings.C_HTML).val());
        return paramsMap;
    }

    public static boolean sendMerchant(String lumber, String clay, String iron, String crop) {
        LOGGER.info("Entering sendMerchant: lumber: " + lumber + ", clay: " + clay + ", iron: " + iron + ", crop: " + crop);
        Map<String, String> paramsToSend = getRequiredParamsForMerchant(AllStrings.LAST_MERCHANT_JSON);
        Response response = getClientFactory().getMerchantClient().postSendMerchant(paramsToSend.get(AllStrings.A_HTML), paramsToSend.get(AllStrings.SZ_PARAM), paramsToSend.get(AllStrings.KID_PARAM), paramsToSend.get(AllStrings.C_HTML),
                lumber, clay, iron, crop);
        return response.getStatus() == 200;
    }

    public static Map<String, String> getResourcesRequiredForNewBuilding(String type) {
        LOGGER.info("Entering getResourcesRequiredForNewBuilding: type: " + type);
        String response = getClientFactory().getBuildingClient().getNewBuildingCategory(AllStrings.getEmptyFieldsList().get(0), "1");
        if (!response.contains("<h2>" + type)) {
            response = getClientFactory().getBuildingClient().getNewBuildingCategory(AllStrings.getEmptyFieldsList().get(0), "2");
            if (!response.contains("<h2>" + type)) {
                response = getClientFactory().getBuildingClient().getNewBuildingCategory(AllStrings.getEmptyFieldsList().get(0), "3");
            }
        }
        Document html = Jsoup.parse(response);
        Element resourcesBuild = html.getElementsContainingOwnText(type).first().parent();
        AllStrings.C_PARAM = getParamsToUpgradeResource(resourcesBuild, false);
        return getResourceRequirements(resourcesBuild);
    }

    public static void getConstructNewBuilding(String type, String id) {
        LOGGER.info("Entering getConstructNewBuilding: type: " + type + ", id: " + id);
        String a = AllStrings.BUILDING_ID_TO_BUILDING_NAME.entrySet().stream().filter(entry -> entry.getValue().contentEquals(type)).findFirst().get().getKey().toString();
        getClientFactory().getBuildingClient().buildNewBuilding(a, id, AllStrings.C_PARAM);
    }

    public static boolean isAttackIncoming() {
        LOGGER.info("Entering isAttackIncoming");
        String response = getClientFactory().getLoginClient().getResources();
        Document html = Jsoup.parse(response);
        Element tableElement = html.getElementById(AllStrings.MOVEMENTS_HTML);
        return tableElement != null && !tableElement.getElementsContainingText(AllStrings.INCOMING_HTML).isEmpty();
    }

    public static Map<String, String> getMilitaryInVillage() {
        LOGGER.info("Entering getMilitaryInVillage");
        String response = getClientFactory().getMilitaryClient().getTroopsVillageActivity("1");
        Document html = Jsoup.parse(response);
        Map<String, String> troopsInVillage = new HashMap<>();
        Elements tableElements = html.getElementsContainingOwnText(AllStrings.TROOPS_IN_VILLAGE_HTML).next().select(AllStrings.TD_UNIT);
        troopsInVillage.put(AllStrings.SOLDIER_PHALANX, tableElements.get(0).text());
        troopsInVillage.put(AllStrings.SOLDIER_SWORDSMAN, tableElements.get(1).text());
        troopsInVillage.put(AllStrings.SOLDIER_PATHFINDER, tableElements.get(2).text());
        troopsInVillage.put(AllStrings.SOLDIER_THEUTATES_THUNDER, tableElements.get(3).text());
        troopsInVillage.put(AllStrings.SOLDIER_DRUIDRIDER, tableElements.get(4).text());
        troopsInVillage.put(AllStrings.SOLDIER_HAEDUAN, tableElements.get(5).text());
        troopsInVillage.put(AllStrings.SOLDIER_RAM, tableElements.get(6).text());
        troopsInVillage.put(AllStrings.SOLDIER_TREBUCHET, tableElements.get(7).text());
        troopsInVillage.put(AllStrings.SOLDIER_CHIEFTAIN, tableElements.get(8).text());
        troopsInVillage.put(AllStrings.SOLDIER_SETTLER, tableElements.get(9).text());
        troopsInVillage.put(AllStrings.SOLDIER_HERO, tableElements.get(10).text());
        return troopsInVillage;
    }

    public static Document postPrepareTroops(String t1, String t2, String t3, String t4, String t5, String t6, String t7, String t8, String t9, String t10, String t11, String x, String y, String attackType) {
        LOGGER.info("Entering postPrepareTroops: attackType: " + attackType);
        String c = "";
        if (attackType.contentEquals(AllStrings.ATTACK_REINFORCEMENT)) {
            c = "2";
        } else if (attackType.contentEquals(AllStrings.ATTACK_NORMAL)) {
            c = "3";
        } else if (attackType.contentEquals(AllStrings.ATTACK_RAID)) {
            c = "4";
        }
        String response = getClientFactory().getMilitaryClient().getTroopsVillageActivity("2");
        Document html = Jsoup.parse(response);
        Element formElement = html.getElementsByAttributeValue(AllStrings.NAME_HTML, AllStrings.SND_HTML).first();
        String timestampValue = getNameValueOfHtmlElement(formElement, AllStrings.TIMESTAMP_HTML);
        String timestampChecksum = getNameValueOfHtmlElement(formElement, AllStrings.CHECKSUM_HTML);
        String currentDid = getNameValueOfHtmlElement(formElement, AllStrings.CURRENT_DID_HTML);
        String prepareTroops = getClientFactory().getMilitaryClient().prepareTroopsForAttack(timestampValue, timestampChecksum, currentDid, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, x, y, c);
        return Jsoup.parse(prepareTroops);
    }

    public static boolean postSendTroops(Document html) {
        LOGGER.info("Entering postSendTroops");
        Element formElement = html.getElementsByTag(AllStrings.FORM_HTML).first();
        String redeployHeroValue = getNameValueOfHtmlElement(formElement, AllStrings.REDEPLOY_HERO_HTML);
        String timestampValue = getNameValueOfHtmlElement(formElement, AllStrings.TIMESTAMP_HTML);
        String checksumValue = getNameValueOfHtmlElement(formElement, AllStrings.CHECKSUM_HTML);
        String idValue = getNameValueOfHtmlElement(formElement, AllStrings.ID_HTML);
        String aValue = getNameValueOfHtmlElement(formElement, AllStrings.A_HTML);
        String cValue = getNameValueOfHtmlElement(formElement, AllStrings.C_HTML);
        String kidValue = getNameValueOfHtmlElement(formElement, AllStrings.KID_PARAM);
        String t1Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_PHALANX);
        String t2Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_SWORDSMAN);
        String t3Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_PATHFINDER);
        String t4Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_THEUTATES_THUNDER);
        String t5Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_DRUIDRIDER);
        String t6Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_HAEDUAN);
        String t7Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_RAM);
        String t8Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_TREBUCHET);
        String t9Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_CHIEFTAIN);
        String t10Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_SETTLER);
        String t11Value = getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_HERO);
        String sendReallyValue = getNameValueOfHtmlElement(formElement, AllStrings.SEND_REALLY_PARAM);
        String troopsSentValue = getNameValueOfHtmlElement(formElement, AllStrings.TROOPS_SENT_PARAM);
        String currentDidValue = getNameValueOfHtmlElement(formElement, AllStrings.CURRENT_DID_HTML);
        String bValue = getNameValueOfHtmlElement(formElement, AllStrings.B_HTML);
        String dnameValue = getNameValueOfHtmlElement(formElement, AllStrings.D_NAME_HTML);
        String xValue = getNameValueOfHtmlElement(formElement, AllStrings.X_PARAM);
        String yValue = getNameValueOfHtmlElement(formElement, AllStrings.Y_PARAM);
        String s1Value = getNameValueOfHtmlElement(formElement, AllStrings.S1_PARAM);
        Response response = getClientFactory().getMilitaryClient().confirmSendTroops(redeployHeroValue, timestampValue, checksumValue, idValue, aValue,
                cValue, kidValue, t1Value, t2Value, t3Value, t4Value, t5Value, t6Value, t7Value, t8Value, t9Value, t10Value, t11Value, sendReallyValue, troopsSentValue,
                currentDidValue, bValue, dnameValue, xValue, yValue, s1Value);
        return response.getStatus() == 302;
    }

    private static String getNameValueOfHtmlElement(Element element, String name) {
        LOGGER.info("Entering getNameValueOfHtmlElement");
        return element.getElementsByAttributeValue(AllStrings.NAME_HTML, name).val();
    }

    private static void setVillageName(Document html) {
        LOGGER.info("Entering setVillageName");
        AllStrings.CURRENT_VILLAGE_NAME = html.getElementById(AllStrings.VILLAGE_NAME_FIELD_HTML).text();
    }

    public static Map<String, String> getInfoAboutTroopsToSend(Document html) {
        LOGGER.info("Entering getInfoAboutTroopsToSend");
        Map<String, String> formInfo = new HashMap<>();
        Element formElement = html.getElementsByTag(AllStrings.FORM_HTML).first();
        formInfo.put(AllStrings.SOLDIER_PHALANX, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_PHALANX));
        formInfo.put(AllStrings.SOLDIER_SWORDSMAN, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_SWORDSMAN));
        formInfo.put(AllStrings.SOLDIER_PATHFINDER, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_PATHFINDER));
        formInfo.put(AllStrings.SOLDIER_THEUTATES_THUNDER, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_THEUTATES_THUNDER));
        formInfo.put(AllStrings.SOLDIER_DRUIDRIDER, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_DRUIDRIDER));
        formInfo.put(AllStrings.SOLDIER_HAEDUAN, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_HAEDUAN));
        formInfo.put(AllStrings.SOLDIER_RAM, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_RAM));
        formInfo.put(AllStrings.SOLDIER_TREBUCHET, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_TREBUCHET));
        formInfo.put(AllStrings.SOLDIER_CHIEFTAIN, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_CHIEFTAIN));
        formInfo.put(AllStrings.SOLDIER_SETTLER, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_SETTLER));
        formInfo.put(AllStrings.SOLDIER_HERO, getNameValueOfHtmlElement(formElement, AllStrings.T_SOLDIER_HERO));
        formInfo.put("Arrival", formElement.getElementsByClass(AllStrings.IN_HTML).text());
        return formInfo;
    }

    public static boolean cancelCurrentBuildTask() {
        LOGGER.info("Entering cancelCurrentBuildTask");
        String response = getClientFactory().getLoginClient().getResources();
        Document fullHtml = Jsoup.parse(response);
        Element finishElem = fullHtml.getElementsByClass(AllStrings.FINISH_NOW_HTML).first();
        String hrefElem = finishElem.nextElementSibling().getElementsByTag(AllStrings.A_HTML).first().attributes().get(AllStrings.HREF_HTML);
        String[] params = hrefElem.replace("?", "").replace("&", "").split("[a-z]=");
        Response cancel = getClientFactory().getBuildingClient().cancelTask(params[1], params[2], params[3]);
        return cancel.getStatus() == 200;
    }

    public static Map<String, String> getMilitaryActivity() {
        LOGGER.info("Entering getMilitaryActivity");
        String response = getClientFactory().getMilitaryClient().getTroopsVillageActivity("1");
        Document fullHtml = Jsoup.parse(response);
        Elements attack = fullHtml.getElementsByClass(AllStrings.OUT_ATTACK_HTML);
        Map<String, String> troopsInAttack = new HashMap<>();
        Element outgoingTroop = fullHtml.getElementsByClass(AllStrings.ABORT_HTML).first();
        if (outgoingTroop == null)
            return troopsInAttack;
        if (!attack.isEmpty()) {
            Element outgoingFirst = attack.first();
            Elements tableElements = outgoingFirst.getElementsByClass(AllStrings.UNITS_HTML).last().select(AllStrings.TD_UNIT);
            AllStrings.CURRENT_ATTACK_OUTGOING = outgoingFirst.getElementsByClass(AllStrings.TROOP_HEADLINE_HTML).first().text();
            AllStrings.TIME_TO_ARRIVE_ATTACK = Integer.parseInt(outgoingFirst.getElementsByClass(AllStrings.TIMER_HTML).val());
//            troopsInAttack.put(AllStrings.SOLDIER_PHALANX, tableElements.get(0).text());
//            troopsInAttack.put(AllStrings.SOLDIER_SWORDSMAN, tableElements.get(1).text());
//            troopsInAttack.put(AllStrings.SOLDIER_PATHFINDER, tableElements.get(2).text());
//            troopsInAttack.put(AllStrings.SOLDIER_THEUTATES_THUNDER, tableElements.get(3).text());
//            troopsInAttack.put(AllStrings.SOLDIER_DRUIDRIDER, tableElements.get(4).text());
//            troopsInAttack.put(AllStrings.SOLDIER_HAEDUAN, tableElements.get(5).text());
//            troopsInAttack.put(AllStrings.SOLDIER_RAM, tableElements.get(6).text());
//            troopsInAttack.put(AllStrings.SOLDIER_TREBUCHET, tableElements.get(7).text());
//            troopsInAttack.put(AllStrings.SOLDIER_CHIEFTAIN, tableElements.get(8).text());
//            troopsInAttack.put(AllStrings.SOLDIER_SETTLER, tableElements.get(9).text());
//            troopsInAttack.put(AllStrings.SOLDIER_HERO, tableElements.get(10).text());
        }
        return troopsInAttack;
    }

    public static boolean cancelMilitaryActivity() {
        LOGGER.info("Entering cancelMilitaryActivity");
        String response = getClientFactory().getMilitaryClient().getTroopsVillageActivity("1");
        Document fullHtml = Jsoup.parse(response);
        Element outgoingTroop = fullHtml.getElementsByClass(AllStrings.ABORT_HTML).first();
        if (outgoingTroop == null)
            return true;
        String href = outgoingTroop.getElementsByTag(AllStrings.BUTTON_HTML).first().attributes().get(AllStrings.ONCLICK_HTML);
        String link = href.substring(href.indexOf("'") + 1, href.lastIndexOf("'"));
        String[] params = link.replace("?", "").replace("&", "").split("[a-z]{0,}=");
        Response cancel = getClientFactory().getMilitaryClient().getCancelTroops(params[2], params[3], params[4]);
        return cancel.getStatus() == 200;
    }

    public static List<String> getAutocompleteVillageName(String name) {
        LOGGER.info("Entering getAutocompleteVillageName: name: " + name);
        Response response = getClientFactory().getMerchantClient().getVillageListToSend(name);
        String jsonResponse = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            VillageNameMerchantJSON merchantJSON2 = mapper.readValue(jsonResponse, VillageNameMerchantJSON.class);
            return merchantJSON2.getResponse().getData();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Map<String, String> getAllVillages() {
        LOGGER.info("Entering getAllVillages");
        String response = getClientFactory().getLoginClient().getResources();
        Map<String, String> villageToId = new HashMap<>();
        Document fullHtml = Jsoup.parse(response);
        Elements elements = fullHtml.getElementsByClass(AllStrings.NAME_HTML);
        for (Element village : elements) {
            if (village.childNodeSize() == 1) {
                String href = village.parents().first().attributes().get(AllStrings.HREF_HTML);
                String newdid = href.substring(href.indexOf("=") + 1, href.lastIndexOf("&"));
                villageToId.put(village.text(), newdid);
            }
        }
        return villageToId;
    }

    public static void changeVillage(String newdid) {
        LOGGER.info("Entering changeVillage: newdid: " + newdid);
        getClientFactory().getLoginClient().changeVillage(newdid);
        AllStrings.BARRACKS_FIELD="";
        AllStrings.STABLE_FIELD="";
        AllStrings.MARKETPLACE_FIELD="";
        AllStrings.RALLY_POINT_FIELD="";
        AllStrings.Z_PARAM="";
        AllStrings.C_PARAM="";
        AllStrings.A_PARAM="";
    }

    public static void getWithdrawArmy(){
        LOGGER.info("Entering getWithdrawArmy");
        String response = getClientFactory().getMilitaryClient().getTroopsVillageActivity("1");
        Document fullHtml = Jsoup.parse(response);
        Element element = fullHtml.getElementsByClass("sback").first();
        String href = element.getElementsByClass("arrow").first().attributes().get("href");
        String[] params = href.replace("?", "").replace("&", "").split("[a-z]{0,}=");
        System.out.println("XD");
        String armyInfo = getClientFactory().getMilitaryClient().getWithdrawArmyView(params[2],params[3]);
    }

    public static boolean postWithdrawArmy(String response, String t1, String t2, String t3, String t4){
        Document fullHtml = Jsoup.parse(response);
        Element buildElem = fullHtml.getElementById("build");
        Element formElem = buildElem.getElementsByTag("form").first();
        String aParam = getNameValueOfHtmlElement(formElem,"a");
        String dParam = getNameValueOfHtmlElement(formElem,"d");
        Response postWithdraw = getClientFactory().getMilitaryClient().withdrawArmyFromVillage("2",aParam,dParam,t1,t2,t3,t4);
        return postWithdraw.getStatus()==302;
    }
}
