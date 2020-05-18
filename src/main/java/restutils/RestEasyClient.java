package restutils;

import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.UriBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

class RestEasyClient {

    private static final Logger LOGGER = LogManager.getLogger(RestEasyClient.class);

    static ResteasyWebTarget createRestEasyObj() {
        LOGGER.info("Entering createRestEasyObj");
        org.jboss.resteasy.client.jaxrs.ResteasyClient client = new ResteasyClientBuilder().sslContext(sslContext()).hostnameVerifier((s, sslSession) -> true).build();
        client.register(new AddHeadersRequestFilter());
        return client.target(UriBuilder.fromPath(AllStrings.PATH));
    }

    private static SSLContext sslContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            // Set up a TrustManager that trusts everything
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                                               String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                }
            }}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.fatal("Exception in SSLContext: " + e.getMessage());
        }
        return sslContext;
    }
}
