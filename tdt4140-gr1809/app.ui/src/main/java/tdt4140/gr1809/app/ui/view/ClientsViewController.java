package tdt4140.gr1809.app.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tdt4140.gr1809.app.client.AccessClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.ui.FxAppController;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

public class ClientsViewController implements Initializable {

	private FxAppController fxAppController;

	private ServiceProvider serviceProvider;

	AccessClient accessClient = new AccessClient();

	@FXML private TableView clientsTable;
	@FXML private TableColumn<User,String> col_lastName;
	@FXML private TableColumn<User,String> col_firstName;
	@FXML private TableColumn<User,LocalDateTime> col_birthdate;
	@FXML private TableColumn<User,String> col_gender;


	@FXML private VBox selectedPersonDetails;
	@FXML private Label lastName;
	@FXML private Label firstName;
	@FXML private Label birthDate;
	@FXML private Label gender;
	@FXML private Label hrMax;



	ObservableList<User> clientsObservableList = FXCollections.observableArrayList();

	public void loadUserList(){
		col_lastName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		col_firstName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		col_birthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate")); //får ikke bort Time delen av LocalDateTime..
		col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

		col_lastName.setOnEditStart(event -> {
			User user = event.getRowValue();
			selectClient(user);});

		clientsObservableList.addAll(accessClient.getUsersServiceProviderHasAccessTo(serviceProvider.getId()));
		clientsTable.setItems(clientsObservableList);
	}

	public void selectClient(User user){
		lastName.setText(user.getLastName());
		firstName.setText(user.getFirstName());
		birthDate.setText(user.getBirthDate().toLocalDate().toString());
		gender.setText(user.getGender());
		hrMax.setText(user.getMaxPulse().toString());
		selectedPersonDetails.setVisible(true);

		fxAppController.user = user;
		fxAppController.enableDataView();
		System.out.println("Selected user: " + user.getId() );
	}

	@Override //slik som det er satt opp nå kan ikke initialize metoden brukes til å laste inn data fordi service provider blir satt i setfxAppController. Ikke bra...
	public void initialize(final URL url, final ResourceBundle rb) {
		selectedPersonDetails.setVisible(false);
	}
	
	public void setfxAppController(FxAppController controller) {
		fxAppController = controller;
		System.out.println(fxAppController);
		serviceProvider = controller.serviceProvider;

		loadUserList();
	}

}
