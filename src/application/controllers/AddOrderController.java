package application.controllers;

import java.sql.SQLException;

import application.Main;
import application.db.entities.BookOrder;
import application.db.entities.Customer;
import application.db.repositories.BookOrderRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddOrderController implements CustomerHolder {
	@FXML
	private TextField ISBNTextField;
	@FXML
	private TextField quantityTextField;
	@FXML
	private TextField publisherNameTextField;

	@FXML
	private Label warningLabel;

	private BookOrderRepo bookOrderRepo = BookOrderRepo.getInstance();

	private Customer customer;

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void addOrder() {
		if (!checkArguments()) {
			warningLabel.setText("*Please fill all fields");
			warningLabel.setVisible(true);
			return;
		}
		warningLabel.setVisible(false);
		BookOrder bookOrder = new BookOrder();
		bookOrder.setISBN(Integer.parseInt(ISBNTextField.getText()));
		bookOrder.setPublisherName(publisherNameTextField.getText());
		bookOrder.setQuantity(Integer.parseInt(quantityTextField.getText()));
		try {
			bookOrderRepo.persist(bookOrder);
		} catch (SQLException e) {
			warningLabel.setText("Cant Confirm Order Please Check Fields");
			warningLabel.setVisible(true);
			e.printStackTrace();
			return;
		}
		switchToCartScene();
	}

	private boolean checkArguments() {
		boolean res = true;
		res = res && !ISBNTextField.getText().isEmpty();
		res = res && !quantityTextField.getText().isEmpty();
		res = res && !publisherNameTextField.getText().isEmpty();
		return res;
	}

	public void back() {
		switchToCartScene();
	}

	private void switchToCartScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.CART_SCENE.getPath()));
		Parent root = null;
		try {
			root = loader.load();
			CartController cartController = loader.getController();
			cartController.setCustomer(customer);
			Main.loadScene(root, Scene.CART_SCENE.getWidth(), Scene.CART_SCENE.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
