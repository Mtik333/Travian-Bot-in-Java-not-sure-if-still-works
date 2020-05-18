package helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static helpers.AllStrings.SET_COOKIE;


public class VariousHelper {

    private static final Logger LOGGER = LogManager.getLogger(VariousHelper.class);

    public static void setCookie(Response response) {
        LOGGER.info("Entering setCookie: response: " + response);
        List<Object> objects = response.getHeaders().get(SET_COOKIE);
        StringBuilder cookieBuilder = new StringBuilder();
        cookieBuilder.append(objects.get(0)).append(";");
        cookieBuilder.append("lowRes=1").append(";");
        String[] atZero = objects.get(2).toString().split(";");
        cookieBuilder.append(atZero[0]).append(";");
        AllStrings.cookie = cookieBuilder.toString();
    }

    public static boolean isEnoughResources(Map<String, String> currentResources, Map<String, String> requiredResources) {
        LOGGER.info("Entering isEnoughResources");
        int currentLumber = Integer.parseInt(currentResources.get(AllStrings.LUMBER).replaceAll("[^0-9]", ""));
        int requiredLumber = Integer.parseInt(requiredResources.get(AllStrings.LUMBER).replaceAll("[^0-9]", ""));
        if (currentLumber < requiredLumber)
            return false;
        int currentClay = Integer.parseInt(currentResources.get(AllStrings.CLAY).replaceAll("[^0-9]", ""));
        int requiredClay = Integer.parseInt(requiredResources.get(AllStrings.CLAY).replaceAll("[^0-9]", ""));
        if (currentClay < requiredClay)
            return false;
        int currentIron = Integer.parseInt(currentResources.get(AllStrings.IRON).replaceAll("[^0-9]", ""));
        int requiredIron = Integer.parseInt(requiredResources.get(AllStrings.IRON).replaceAll("[^0-9]", ""));
        if (currentIron < requiredIron)
            return false;
        int currentCrop = Integer.parseInt(currentResources.get(AllStrings.CROP).replaceAll("[^0-9]", ""));
        int requiredCrop = Integer.parseInt(requiredResources.get(AllStrings.CROP).replaceAll("[^0-9]", ""));
        return currentCrop >= requiredCrop;
    }
}
