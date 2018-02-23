package tdt4140.gr1809.app.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;


public class FxAppController implements Initializable {
	@FXML private TextField testTextField;
	
	
	
	@FXML
	private void initialLoginRequest() {
		testTextField.setText(testTextField.getText() + "hei");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Kode som starter når man åpner applikasjonen
		
	}
}
