package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.client.NotificationClient;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.core.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationsViewController implements Initializable{

	private FxAppController fxAppController;
	@FXML ListView<Notification> NotificationsList;
	@FXML TextArea Notification;
	private NotificationClient client = new NotificationClient();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateNotification() {
		Notification note = NotificationsList.getSelectionModel().getSelectedItem();
		Notification.setText(note.getMessage());
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		List<Notification> notificationList = client.getNotificationByUserId(fxAppController.user.getId());
		NotificationsList.setItems(FXCollections.observableArrayList(notificationList));
	}

}
