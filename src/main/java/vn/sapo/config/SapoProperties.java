package vn.sapo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sapo", ignoreUnknownFields = false)
public class SapoProperties {

    private final Client client = new Client();

    public Client getClient() {
        return client;
    }

    public static class Client {

        private String clientId = SapoDefaults.Client.clientId;
        private String clientSecret = SapoDefaults.Client.clientSecret;

        public String getClientId() {
            return clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }
    }

}
