package restutils;

import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import static helpers.AllStrings.COOKIE;

public class AddHeadersRequestFilter implements ClientRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(AddHeadersRequestFilter.class);

    @Override
    public void filter(ClientRequestContext clientRequestContext) {
        if (!AllStrings.cookie.isEmpty()) {
            LOGGER.info("Adding cookie: " + AllStrings.cookie);
            clientRequestContext.getHeaders().add(COOKIE, AllStrings.cookie);
        }
    }

}
