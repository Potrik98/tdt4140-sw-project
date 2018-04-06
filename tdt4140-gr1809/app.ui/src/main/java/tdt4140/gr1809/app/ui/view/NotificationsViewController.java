package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.client.NotificationClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.core.model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class NotificationsViewController implements Initializable{

	private FxAppController fxAppController;
	@FXML ListView<Notification> NotificationsList;
	@FXML TextArea NotificationText;
	private NotificationClient client = new NotificationClient();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateNotification() {
		Notification note = NotificationsList.getSelectionModel().getSelectedItem();
		NotificationText.setText(note.getMessage());
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		
		final Notification eksempel = Notification.builder()
			.message("DU MÅ TIL LEGEN DU HAR PULS OVER 9000 BPM!!!")
			.userId(fxAppController.user.getId())
			.id(UUID.randomUUID())
			.time(LocalDateTime.now())
			.build();
		NotificationClient client2 = new NotificationClient();
		client2.createNotification(eksempel);
		
		List<Notification> notificationList = client.getNotificationByUserId(fxAppController.user.getId());
		NotificationsList.setItems(FXCollections.observableArrayList(notificationList));
	}

}
