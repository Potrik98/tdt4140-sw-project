package tdt4140.gr1809.app.ui.view;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import tdt4140.gr1809.app.client.NotificationClient;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

public class NotifyUserViewController implements Initializable {
	private FxAppController fxAppController;
	
	
	@FXML TextField notetime;
	@FXML TextArea NotificationText;
	private NotificationClient client = new NotificationClient();
	private LocalDateTime datetime;
	
	
	@FXML
	public void createUserNotification() {
		String text = NotificationText.getText();
		Notification note = Notification.builder()
				.userId(fxAppController.user.getId())
				.message(text)
				.time(datetime)
				.build();
		client.createNotification(note);
		NotificationText.setText("");
		notetime.setText("");
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		
		datetime = datetime.now();
		notetime.setText(datetime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " at " + datetime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
