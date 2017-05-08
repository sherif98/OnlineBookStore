package application.controllers;

import java.util.List;
import java.util.stream.Collectors;

import application.Main;
import application.db.entities.Cart;
import application.db.entities.Customer;
import application.db.repositories.CartRepo;
import application.dto.CartDTO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class CartController implements CustomerHolder {
	private static final int PAGE_SIZE = 20;

	@FXML
	private TableView<CartDTO> tableView;
	@FXML
	private VBox vbox;
	@FXML
	private Label warningLabel;
	@FXML
	private TextField ISBNTextField;
	@FXML
	private TextField quantityField;
	@FXML
	private TextField cartIdTextField;
	@FXML
	private DatePicker expireDatePicker;
	@FXML
	private TextField creditCardTextField;
	@FXML
	private Label cartPageNumberLabel;

	private Customer customer;
	private int cartPageNumber;
	private CartRepo cartRepo = CartRepo.getInstance();

	@FXML
	public void initialize() {
		ObservableList<TableColumn<CartDTO, ?>> columns = tableView.getColumns();
		columns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		columns.get(1).setCellValueFactory(new PropertyValueFactory<>("quantity"));
		columns.get(2).setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
		columns.get(3).setCellValueFactory(new PropertyValueFactory<>("ISBN"));

		cartPageNumberLabel.setText(String.valueOf(cartPageNumber + 1));
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		if (customer != null) {
			this.vbox.setVisible(customer.isManager());
			reloadTable();
		}
	}

	public void addCart() {
		if (!checkArgs()) {
			warningLabel.setText("*Please fill all parameters");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			int ISBN = Integer.parseInt(ISBNTextField.getText());
			int quantity = Integer.parseInt(quantityField.getText());
			cartRepo.buyBook(customer.getId(), ISBN, quantity);
		} catch (Exception e) {
			warningLabel.setText("Error Happend will adding to cart");
			warningLabel.setVisible(true);
			System.out.println("CartController: Error Happend in addCart");
			e.printStackTrace();
		}
	}

	public void checkOut() {
		if (!checkCheckOutArgs()) {
			warningLabel.setText("*Please fill all check out fields");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			cartRepo.checkOut(customer.getId());
		} catch (Exception e) {
			warningLabel.setText("*Error happend while check out");
			warningLabel.setVisible(true);
			System.out.println("CartController: error happend in check out");
			e.printStackTrace();
		}
	}

	private boolean checkCheckOutArgs() {
		boolean res = true;
		res = res && (!creditCardTextField.getText().isEmpty());
		res = res && (expireDatePicker.getValue() != null);
		// TODO check the date
		return res;
	}

	public void removeCart() {
		if (!checkRemoveCartArgs()) {
			warningLabel.setText("*Please fill Cart ID field");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			int cartId = Integer.parseInt(cartIdTextField.getText());
			cartRepo.deleteCart(cartId);
		} catch (Exception e) {
			warningLabel.setText("*Error will deleting cart");
			warningLabel.setVisible(true);
			System.out.println("CartController: Error Happend in removeCart");
			e.printStackTrace();
		}
	}

	private boolean checkRemoveCartArgs() {
		boolean res = true;
		res = res && (!cartIdTextField.getText().isEmpty());
		return res;
	}

	public boolean reloadTable() {
		try {
			warningLabel.setVisible(false);
			List<CartDTO> cartDTO = cartRepo.selectByCustomerIdPaging(customer.getId(), cartPageNumber, PAGE_SIZE)
					.stream().map(CartDTO::new).collect(Collectors.toList());
			if (cartDTO.isEmpty() && cartPageNumber > 0) {
				return false;
			}
			tableView.getItems().clear();
			tableView.getItems().addAll(cartDTO);
			return true;
		} catch (Exception e) {
			warningLabel.setText("*Error happend while reloading the table");
			warningLabel.setVisible(true);
			System.out.println("Cart Controller: Err in reload table");
			e.printStackTrace();
		}
		return false;
	}

	public void cartNextPage() {
		++cartPageNumber;
		boolean res = reloadTable();
		if (!res) {
			--cartPageNumber;
			return;
		}
		cartPageNumberLabel.setText(String.valueOf(cartPageNumber + 1));
	}

	public void cartPrevPage() {
		if (cartPageNumber <= 0) {
			return;
		}
		--cartPageNumber;
		boolean res = reloadTable();
		if (!res) {
			++cartPageNumber;
			return;
		}
		cartPageNumberLabel.setText(String.valueOf(cartPageNumber + 1));
	}

	private boolean checkArgs() {
		boolean res = true;
		res = res && (!ISBNTextField.getText().isEmpty());
		res = res && (!quantityField.getText().isEmpty());
		return res;
	}

	public void switchToLoginScene() {
		try {
			List<Cart> carts = cartRepo.selectByCustomerId(customer.getId());
			for (Cart cart : carts) {
				cartRepo.deleteCart(cart.getId());
			}
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.LOGIN_SCENE.getPath()));
			Parent root = loader.load();
			Main.loadScene(root, Scene.LOGIN_SCENE.getWidth(), Scene.LOGIN_SCENE.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToEditProfileScene() {
		switchToScene(Scene.EDIT_PROFILE_SCENE);
	}

	public void switchToSearchBooksScene() {
		switchToScene(Scene.SEARCH_BOOKS_SCENE);
	}

	public void switchToAddBookScene() {
		switchToScene(Scene.ADD_BOOK_SCENE);
	}

	public void switchToManagerScene() {
		switchToScene(Scene.MANAGER_SCENE);
	}

	public void switchToAddPublisherScene() {
		switchToScene(Scene.ADD_PUBLISHER_SCENE);
	}

	public void switchToReportScene() {
		switchToScene(Scene.REPORT_SCENE);
	}

	public void switchToAddOrderScene() {
		switchToScene(Scene.ADD_ORDER_SCENE);
	}

	private void switchToScene(final Scene scene) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(scene.getPath()));
			Parent root = loader.load();
			CustomerHolder customerHolder = loader.getController();
			customerHolder.setCustomer(customer);
			Main.loadScene(root, scene.getWidth(), scene.getHeight());
		} catch (Exception e) {
			System.out.println("Error in Loading sign up scene");
			e.printStackTrace();
		}
	}
	
	public void refreshHandler(){
		cartPageNumber = 0;
		reloadTable();
	}
}
