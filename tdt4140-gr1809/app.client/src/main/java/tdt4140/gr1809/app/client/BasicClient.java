package tdt4140.gr1809.app.client;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BasicClient {
    protected final ResteasyWebTarget target;

    public BasicClient() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("client.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String serverUri = properties.getProperty("serverurl");
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(serverUri);
    }

    public BasicClient(final String serverUri) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        target = client.target(serverUri);
    }
}
