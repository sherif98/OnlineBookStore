package application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import application.Main;
import application.db.entities.Customer;
import application.db.repositories.SoldRepo;
import application.dto.TopBookDTO;
import application.dto.TopCustomerDTO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReportController implements CustomerHolder {
	@FXML
	private TableView<TopCustomerDTO> topCustomerTable;
	@FXML
	private TableView<TopBookDTO> topBooksTable;
	@FXML
	private TextField priceTextField;
	@FXML
	private Label warningLabel;

	private Customer customer;
	private SoldRepo soldRepo = SoldRepo.getInstance();
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@FXML
	public void initialize() {
		ObservableList<TableColumn<TopCustomerDTO, ?>> topCustomerCols = topCustomerTable.getColumns();
		topCustomerCols.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		topCustomerCols.get(1).setCellValueFactory(new PropertyValueFactory<>("firstname"));
		topCustomerCols.get(2).setCellValueFactory(new PropertyValueFactory<>("lastname"));
		topCustomerCols.get(3).setCellValueFactory(new PropertyValueFactory<>("totalCash"));

		ObservableList<TableColumn<TopBookDTO, ?>> topBooksCols = topBooksTable.getColumns();
		topBooksCols.get(0).setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		topBooksCols.get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
		topBooksCols.get(2).setCellValueFactory(new PropertyValueFactory<>("publisherName"));
		topBooksCols.get(3).setCellValueFactory(new PropertyValueFactory<>("soldCopies"));

		loadTopCustomers();
		loadTopBooks();
		loadPrice();
	}

	private void loadPrice() {
		try {
			warningLabel.setVisible(false);
			priceTextField.setText(String.valueOf(soldRepo.getLastMonthProfit()));
		} catch (Exception e) {
			warningLabel.setText("*Error while loading price");
			warningLabel.setVisible(true);
			e.printStackTrace();
		}
	}

	private void loadTopBooks() {
		try {
			warningLabel.setVisible(false);
			topBooksTable.getItems().clear();
			List<TopBookDTO> topBooks = soldRepo.selectTopTenBooks().stream().map(TopBookDTO::new)
					.collect(Collectors.toList());
			topBooksTable.getItems().addAll(topBooks);
		} catch (Exception e) {
			warningLabel.setText("*Error while loading books");
			warningLabel.setVisible(true);
			e.printStackTrace();
		}
	}

	private void loadTopCustomers() {
		try {
			warningLabel.setVisible(false);
			topCustomerTable.getItems().clear();
			List<TopCustomerDTO> topCustomers = soldRepo.selectTopFiveCustomers().stream().map(TopCustomerDTO::new)
					.collect(Collectors.toList());
			topCustomerTable.getItems().addAll(topCustomers);
		} catch (Exception e) {
			warningLabel.setText("*Error while loading customers");
			warningLabel.setVisible(true);
			e.printStackTrace();
		}
	}
	
	public void switchToCartScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.CART_SCENE.getPath()));
			Parent root = loader.load();
			CartController cartController = loader.getController();
			cartController.setCustomer(customer);
			Main.loadScene(root, Scene.CART_SCENE.getWidth(), Scene.CART_SCENE.getHeight());
		} catch (Exception e) {
			System.out.println("Error in Loading cart scene");
			e.printStackTrace();
		}
	}
}
