package tdt4140.gr1809.app.ui.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {

	private FxAppController fxAppController;

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {

	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		System.out.println(fxAppController);
		User user = fxAppController.user;
		ServiceProvider sp = fxAppController.serviceProvider;

	}

}
