package tdt4140.gr1809.app.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BasicClient {
    protected final WebTarget target;

    public BasicClient(final String resource) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("client.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String serverUri = properties.getProperty("serverurl")
                .concat(resource);

        final Client client = ClientBuilder.newClient();
        target = client.target(serverUri);
    }
}
