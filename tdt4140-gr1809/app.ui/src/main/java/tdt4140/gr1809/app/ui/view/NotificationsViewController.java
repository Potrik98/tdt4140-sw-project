package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.*;
import tdt4140.gr1809.app.client.NotificationClient;
import tdt4140.gr1809.app.ui.FxAppController;
import tdt4140.gr1809.app.core.model.*;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class NotificationsViewController implements Initializable{

	@FXML ListView<Notification> NotificationsListView = new ListView<Notification>();
	@FXML TextArea NotificationText;
	@FXML TextField itemDateTime;
	
	private FxAppController fxAppController;
	private NotificationClient client = new NotificationClient();
	private ObservableList<Notification> notificationList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	public void updateNotification() {
		Notification note = NotificationsListView.getFocusModel().getFocusedItem();
		if(note != null) {
			NotificationText.setText(note.getMessage());
			itemDateTime.setText(note.getTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " at " + note.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		}
	}
	@FXML
	public void deleteNotification() throws IOException {
		Notification note = NotificationsListView.getFocusModel().getFocusedItem();
		client.deleteNotification(note.getId());
		fxAppController.goToNotificationsView(null);
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		notificationList = FXCollections.observableArrayList(client.getNotificationsByUserId(fxAppController.user.getId()));
		System.out.println(notificationList);
		NotificationsListView.setItems(notificationList);
		NotificationsListView.setCellFactory(param -> new ListCell<Notification>() {
		    @Override
		    protected void updateItem(Notification item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null || item.getMessage() == null) {
		            setText(null);
		        } else {
		        		setText(item.getMessage());
		        }
		    }
		});
	}

}
