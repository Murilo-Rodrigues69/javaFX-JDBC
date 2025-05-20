package gui;

import gui.listeners.DataChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departament;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartamentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private DepartamentService deparatamentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtname;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Departament> comboBoxDepartament;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Departament> obsList;

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartamentService dpservice) {
		this.service = service;
		this.deparatamentService = dpservice;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Eror saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtname.getText() == null || txtname.getText().trim().equals("")) {
			exception.addError("Name", "Field can´t be empty");
		}
		obj.setName(txtname.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;

	}

	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtname, 50);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtname.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(entity.getDepartment() == null) {
			comboBoxDepartament.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartament.setValue(entity.getDepartment());
		}
	}

	public void loadAssociateObjects() {
		if (deparatamentService == null) {
			throw new IllegalStateException("DeparatamentService was null");
		}
		List<Departament> list = deparatamentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartament.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> error) {
		Set<String> fields = error.keySet();

		if (fields.contains("Name")) {
			labelErrorName.setText(error.get("Name"));
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Departament>, ListCell<Departament>> factory = lv -> new ListCell<Departament>() {
			@Override
			protected void updateItem(Departament item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartament.setCellFactory(factory);
		comboBoxDepartament.setButtonCell(factory.call(null));
	}

}
