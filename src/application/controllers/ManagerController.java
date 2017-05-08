package application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import application.Main;
import application.db.entities.Customer;
import application.db.repositories.BookOrderRepo;
import application.db.repositories.CustomerRepo;
import application.dto.BookOrderDTO;
import application.dto.CustomerDTO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManagerController implements CustomerHolder {
	private static final int PAGE_SIZE = 10;

	@FXML
	private TextField customerIdTextField;
	@FXML
	private TextField orderIdTextField;
	@FXML
	private TableView<CustomerDTO> customersTableView;
	@FXML
	private TableView<BookOrderDTO> bookOrdersTableView;
	@FXML
	private Label warningLabel;
	@FXML
	private Label customerPageNumberLabel;
	@FXML
	private Label ordersPageNumberLabel;

	private CustomerRepo customerRepo = CustomerRepo.getInstance();
	private BookOrderRepo bookOrderRepo = BookOrderRepo.getInstance();
	private Customer customer;
	private int customerPageNumber;
	private int ordersPageNumber;

	@FXML
	public void initialize() {
		ObservableList<TableColumn<CustomerDTO, ?>> customerTableColumns = customersTableView.getColumns();
		customerTableColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		customerTableColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("username"));
		customerTableColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("firstname"));
		customerTableColumns.get(3).setCellValueFactory(new PropertyValueFactory<>("isManager"));
		customerTableColumns.get(4).setCellValueFactory(new PropertyValueFactory<>("email"));
		customerTableColumns.get(5).setCellValueFactory(new PropertyValueFactory<>("shippingAdress"));
		customerTableColumns.get(6).setCellValueFactory(new PropertyValueFactory<>("phone"));

		ObservableList<TableColumn<BookOrderDTO, ?>> bookOrderTableColumns = bookOrdersTableView.getColumns();
		bookOrderTableColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		bookOrderTableColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		bookOrderTableColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
		bookOrderTableColumns.get(3).setCellValueFactory(new PropertyValueFactory<>("publisherName"));

		reloadBookOrders();
		reloadCustomers();
		customerPageNumberLabel.setText(String.valueOf(customerPageNumber + 1));
		ordersPageNumberLabel.setText(String.valueOf(ordersPageNumber + 1));
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void promotCustomer() {
		if (!checkPromotCustomerArgs()) {
			warningLabel.setText("*Please enter valid customer id");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			int customerId = Integer.parseInt(customerIdTextField.getText());
			customerRepo.promot(customerId);
		} catch (Exception e) {
			warningLabel.setText("*Error happend during promotion");
			warningLabel.setVisible(true);
			System.out.println("ManagerController: error in promot customer");
			e.printStackTrace();
		}
	}

	public void confirmOrder() {
		if (!checkConfirmOrderArgs()) {
			warningLabel.setText("*Please enter valid order ID");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			int orderId = Integer.parseInt(orderIdTextField.getText());
			bookOrderRepo.deleteById(orderId);
		} catch (Exception e) {
			warningLabel.setText("*Error happend during confirming the order");
			warningLabel.setVisible(true);
			System.out.println("ManagerController: error in confirm order");
			e.printStackTrace();
		}
	}

	public boolean reloadCustomers() {
		try {
			warningLabel.setVisible(false);
			List<CustomerDTO> customerDTOs = customerRepo.selectWithPaging(customerPageNumber, PAGE_SIZE).stream()
					.map(CustomerDTO::new).collect(Collectors.toList());
			if (customerDTOs.isEmpty() && customerPageNumber > 0)
				return false;
			customersTableView.getItems().clear();
			customersTableView.getItems().addAll(customerDTOs);
			return true;
		} catch (Exception e) {
			warningLabel.setText("*Error will loading customers");
			warningLabel.setVisible(true);
			System.out.println("Error while loading customers");
			e.printStackTrace();
		}
		return false;
	}

	public boolean reloadBookOrders() {
		try {
			List<BookOrderDTO> bookOrderDTOs = bookOrderRepo.selectWithPaging(ordersPageNumber, PAGE_SIZE).stream()
					.map(BookOrderDTO::new).collect(Collectors.toList());
			if (bookOrderDTOs.isEmpty() && ordersPageNumber > 0) {
				return false;
			}
			bookOrdersTableView.getItems().clear();
			bookOrdersTableView.getItems().addAll(bookOrderDTOs);
			return true;
		} catch (Exception e) {
			warningLabel.setText("*Error happend will loading order table");
			System.out.println("Manager Controller: Error in loading book orders");
			e.printStackTrace();
		}
		return false;
	}

	public void customerNextPage() {
		++customerPageNumber;
		boolean res = reloadCustomers();
		if (!res) {
			--customerPageNumber;
			return;
		}
		customerPageNumberLabel.setText(String.valueOf(customerPageNumber + 1));
	}

	public void customerPrevPage() {
		if (customerPageNumber <= 0) {
			return;
		}
		--customerPageNumber;
		boolean res = reloadCustomers();
		if (!res) {
			++customerPageNumber;
		}
		customerPageNumberLabel.setText(String.valueOf(customerPageNumber + 1));
	}
	
	public void ordersNextPage() {
		++ordersPageNumber;
		boolean res = reloadBookOrders();
		if (!res) {
			--ordersPageNumber;
		}
		ordersPageNumberLabel.setText(String.valueOf(ordersPageNumber + 1));
	}
	
	public void ordersPrevPage() {
		if (ordersPageNumber <= 0) {
			return;
		}
		--ordersPageNumber;
		boolean res = reloadBookOrders();
		if (!res) {
			++ordersPageNumber;
		}
		ordersPageNumberLabel.setText(String.valueOf(ordersPageNumber + 1));
	}

	private boolean checkConfirmOrderArgs() {
		boolean res = true;
		res = res && (!orderIdTextField.getText().isEmpty());
		return res;
	}

	private boolean checkPromotCustomerArgs() {
		boolean res = true;
		res = res && (!customerIdTextField.getText().isEmpty());
		return res;
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
	
	public void refreshCustomerHandler(){
		customerPageNumber = 0;
		reloadCustomers();
	}
	
	public void refreshOrderHandler(){
		ordersPageNumber = 0;
		reloadBookOrders();
	}
}
