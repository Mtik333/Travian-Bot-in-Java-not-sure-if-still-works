package merchant;

import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import restutils.AbstractClient;
import restutils.AllRequestsHelper;

import javax.ws.rs.core.Response;

public class MerchantClient extends AbstractClient<MerchantService> {

    private static final Logger LOGGER = LogManager.getLogger(MerchantClient.class);

    public MerchantClient(ResteasyWebTarget stub) {
        super(stub, MerchantService.class);
    }

    public Response postPrepareMarketplace(String lumber, String clay, String iron, String crop, String x, String y, String dname) {
        LOGGER.info("Entering postPrepareMarketplace");
        if (AllStrings.MARKETPLACE_FIELD.isEmpty()) {
            AllStrings.setMarketplaceField(AllStrings.MARKETPLACE);
        }
        if (AllStrings.AJAX_PARAM.isEmpty()) {
            AllRequestsHelper.ajaxParam();
        }
        return execute(() -> service.prepareMarketplace(AllStrings.PREPARE_MARKETPLACE_PARAM, AllStrings.PREPARE_MARKETPLACE_PARAM, lumber, clay, iron, crop, dname, x, y, AllStrings.MARKETPLACE_FIELD, "5", "1", AllStrings.AJAX_PARAM));
    }

    public Response postSendMerchant(String a, String sz, String kid, String c, String lumber, String clay, String iron, String crop) {
        LOGGER.info("Entering postSendMerchant");
        return execute(() -> service.sendMerchant(AllStrings.PREPARE_MARKETPLACE_PARAM, AllStrings.PREPARE_MARKETPLACE_PARAM, "5", AllStrings.MARKETPLACE_FIELD, a, sz, kid, c, "1", lumber, clay, iron, crop, AllStrings.AJAX_PARAM));
    }

    public String getMarketplaceHtmlSendResources() {
        LOGGER.info("Entering getMarketplaceHtmlSendResources");
        return execute(() -> service.getSendResourcesHtml("5", AllStrings.MARKETPLACE_FIELD));
    }

    public Response getVillageListToSend(String name) {
        LOGGER.info("Entering getVillageListToSend, name: " + name);
        if (AllStrings.AJAX_PARAM.isEmpty()) {
            AllRequestsHelper.ajaxParam();
        }
        return execute(() -> service.getVillageListToSend("autoComplete", "autoComplete", "villagename", name, AllStrings.AJAX_PARAM));
    }
}
