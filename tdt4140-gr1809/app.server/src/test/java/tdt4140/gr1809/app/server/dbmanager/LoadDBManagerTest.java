package tdt4140.gr1809.app.server.dbmanager;

import org.junit.Test;

public class LoadDBManagerTest {
    @Test
    public void testLoadCreateScript() throws Exception {
        DBManager.loadCreateScript();
    }

    @Test
    public void testCreateDBManagerFromPropertiesFile() throws Exception {
        DBManager dbManager = new DataDBManager();
        dbManager.closeConnection();
    }
}
