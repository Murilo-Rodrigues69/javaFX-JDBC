package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departament;
import model.services.DepartamentService;

public class DepartamentListController implements Initializable {

	private DepartamentService service;
	
	@FXML
	private TableView<Departament> tableViewDepartaments;
	
	@FXML
	private TableColumn<Departament, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departament, String >tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Departament> obsList;
	
	
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setDepartamentService(DepartamentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewDepartaments.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Departament> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartaments.setItems(obsList);
	}

}
