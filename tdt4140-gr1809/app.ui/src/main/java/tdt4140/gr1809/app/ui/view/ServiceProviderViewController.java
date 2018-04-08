package tdt4140.gr1809.app.ui.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.client.AccessClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.UUID;

public class ServiceProviderViewController implements Initializable {

	@FXML private ComboBox<ServiceProvider> ServiceProvidersComboBox;
	@FXML private TextField serviceproviderID;
	
	private FxAppController fxAppController;
	AccessClient accessclient = new AccessClient();

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {

	}
	
	@FXML
	private void grantServiceProviderAccess() throws IOException {
		String SPID = serviceproviderID.getText();
		UUID spuuid = UUID.fromString(SPID);
		accessclient.giveServiceProviderAccessToUser(spuuid, fxAppController.user.getId());
		fxAppController.goToServiceProviderView(null);

	}
	
	@FXML
	private void revokeServiceProviderAccess() throws IOException {
		ServiceProvider provider = ServiceProvidersComboBox.getValue();
		accessclient.revokeServiceProviderAccessToUser(provider.getId(), fxAppController.user.getId());
		fxAppController.goToServiceProviderView(null);
	}
	
	@SuppressWarnings("restriction")
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		List<ServiceProvider> gg = accessclient.getServiceProviderWithAccessToUser(fxAppController.user.getId());
		ServiceProvidersComboBox.setItems(FXCollections.observableArrayList(gg));
		ServiceProvidersComboBox.setConverter(new StringConverter<ServiceProvider>() {
			
			public String toString(ServiceProvider object) {
		        return object.getLastName();
		    }

		    public ServiceProvider fromString(String string) {
		        for (ServiceProvider provider : gg) {
		        	if(string == provider.getLastName()) {
		        		return provider;
		        	}
		        }
		        return null;
		    }
		});
	}

}
