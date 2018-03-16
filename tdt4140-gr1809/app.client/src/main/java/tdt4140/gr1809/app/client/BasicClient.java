package tdt4140.gr1809.app.client;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import tdt4140.gr1809.app.core.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BasicClient {
    protected final ResteasyWebTarget target;

    public BasicClient() {
        InputStream input = BasicClient.class.getResourceAsStream("client.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String serverUri = properties.getProperty("serverurl");

        ResteasyJackson2Provider resteasyJackson2Provider = new ResteasyJackson2Provider();
        resteasyJackson2Provider.setMapper(User.mapper);
        ResteasyClient client = new ResteasyClientBuilder().register(resteasyJackson2Provider).build();
        target = client.target(serverUri);

        System.out.println("Opened webtarget connection to " + serverUri);
    }
}
