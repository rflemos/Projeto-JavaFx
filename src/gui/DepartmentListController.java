package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment; 
	
	@FXML
	private TableColumn<Department, Integer> tableColumId;
	
	@FXML
	private TableColumn<Department, String> tableColumName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage );
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeNodes();
		
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	private void initializeNodes() {
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		//padr�o javaFX iniciar o comportamento das colunas;
		
		
		//faz o Table View Acompanhar a altura da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list); // instacia obsList usando os dados da lista
		tableViewDepartment.setItems(obsList);
	}
	
	
	private void createDialogForm(Department obj, String absoluteName , Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // carrega a minha view
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.updateData();
			
			
			//para carregar janela de dialogo modal e necessario instaciar um outro stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");// configurando stage
			dialogStage.setScene(new Scene(pane));// passo minha view como uma nova scene
			dialogStage.setResizable(false);//janela n�o pode ser redimensionada
			dialogStage.initOwner(parentStage);//Stage pai dessa janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// equanto a janela n�o e fechado n�o se pode acessar outra.
			dialogStage.showAndWait();
			
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception","Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
