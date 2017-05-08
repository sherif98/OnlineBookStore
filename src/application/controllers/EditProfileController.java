package application.controllers;

import java.sql.SQLException;

import application.Main;
import application.db.entities.Customer;
import application.db.repositories.CustomerRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditProfileController implements CustomerHolder {

	@FXML
	private TextField firstNameTextField;
	@FXML
	private TextField lastNameTextField;
	@FXML
	private TextField userNameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private TextField addressTextField;
	@FXML
	private TextField phoneTextField;
	@FXML
	private Label warningLabel;

	private CustomerRepo customerRepo = CustomerRepo.getInstance();

	private Customer customer;

	public void setCustomer(Customer customer) {
		this.customer = customer;
		if (customer != null) {
			firstNameTextField.setText(customer.getFirstname());
			lastNameTextField.setText(customer.getLastname());
			userNameTextField.setText(customer.getUsername());
			passwordTextField.setText(customer.getPassword());
			emailTextField.setText(customer.getEmail());
			addressTextField.setText(customer.getShippingAddress());
			phoneTextField.setText(customer.getPhone());
		}
	}

	public void updateProfile() {
		if (!checkArguments()) {
			warningLabel.setText("*Please fill all fields");
			warningLabel.setVisible(true);
			return;
		}
		warningLabel.setVisible(false);
		Customer updatedCustomer = new Customer();
		updatedCustomer.setId(customer.getId());
		updatedCustomer.setManager(customer.isManager());
		updatedCustomer.setFirstname(firstNameTextField.getText());
		updatedCustomer.setLastname(lastNameTextField.getText());
		updatedCustomer.setEmail(emailTextField.getText());
		updatedCustomer.setUsername(userNameTextField.getText());
		updatedCustomer.setPassword(passwordTextField.getText());
		updatedCustomer.setPhone(phoneTextField.getText());
		updatedCustomer.setShippingAddress(addressTextField.getText());
		try {
			customerRepo.update(updatedCustomer);
		} catch (SQLException e) {
			warningLabel.setText("Cant Update Data");
			warningLabel.setVisible(true);
			e.printStackTrace();
			return;
		}
		customer = updatedCustomer;
		switchToCartScene();
	}

	private boolean checkArguments() {
		boolean res = true;
		res = res && !firstNameTextField.getText().isEmpty();
		res = res && !lastNameTextField.getText().isEmpty();
		res = res && !userNameTextField.getText().isEmpty();
		res = res && !passwordTextField.getText().isEmpty();
		res = res && !emailTextField.getText().isEmpty();
		res = res && !addressTextField.getText().isEmpty();
		res = res && !phoneTextField.getText().isEmpty();
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
