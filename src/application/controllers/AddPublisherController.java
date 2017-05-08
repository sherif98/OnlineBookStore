package application.controllers;

import java.sql.SQLException;

import application.Main;
import application.db.entities.Customer;
import application.db.entities.Publisher;
import application.db.repositories.PublisherRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddPublisherController implements CustomerHolder {

	@FXML
	private TextField publisherNameTextField;

	@FXML
	private TextField addressTextField;

	@FXML
	private TextField phoneTextField;

	@FXML
	private Label warningLabel;

	private PublisherRepo publisherRepo = PublisherRepo.getInstance();

	private Customer customer;

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void addPublisher() {
		if (!checkArguments()) {
			warningLabel.setText("*Please fill all fields");
			warningLabel.setVisible(true);
			return;
		}
		warningLabel.setVisible(false);
		Publisher publisher = new Publisher();
		publisher.setPublisherName(publisherNameTextField.getText());
		publisher.setAddress(addressTextField.getText());
		publisher.setPhone(phoneTextField.getText());
		try {
			publisherRepo.persist(publisher);
		} catch (SQLException e) {
			warningLabel.setText("Cant insert publisher");
			warningLabel.setVisible(true);
			System.out.println("Error cant insert publisher");
			e.printStackTrace();
			return;
		}
		switchToCartScene();
	}

	private boolean checkArguments() {
		boolean res = true;
		res = res && !publisherNameTextField.getText().isEmpty();
		res = res && !phoneTextField.getText().isEmpty();
		res = res && !addressTextField.getText().isEmpty();
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
