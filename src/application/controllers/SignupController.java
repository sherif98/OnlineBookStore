package application.controllers;

import application.Main;
import application.db.entities.Customer;
import application.db.repositories.CustomerRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
	private static final Scene LOGIN_SCENE = Scene.LOGIN_SCENE;

	@FXML
	private TextField firstname;
	@FXML
	private TextField lastname;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private TextField email;
	@FXML
	private TextField address;
	@FXML
	private TextField phone;
	@FXML
	private Label warningLabel;

	private CustomerRepo customerRepo = CustomerRepo.getInstance();

	public void addCustomer() {
		if (!checkArguments()) {
			warningLabel.setVisible(true);
			return;
		}
		warningLabel.setVisible(false);
		Customer customer = new Customer();
		customer.setEmail(email.getText());
		customer.setFirstname(firstname.getText());
		customer.setLastname(lastname.getText());
		customer.setPassword(password.getText());
		customer.setShippingAddress(address.getText());
		customer.setPhone(phone.getText());
		customer.setUsername(username.getText());
		customer.setManager(false);
		try {
			customerRepo.persist(customer);
			FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_SCENE.getPath()));
			Main.loadScene(loader.load(), LOGIN_SCENE.getWidth(), LOGIN_SCENE.getHeight());
			System.out.println("Sign up successfully");
			System.out.println("new customer object: " + customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToLoginScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_SCENE.getPath()));
			Main.loadScene(loader.load(), LOGIN_SCENE.getWidth(), LOGIN_SCENE.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkArguments() {
		boolean res = true;
		res = res && (!firstname.getText().isEmpty());
		res = res && (!lastname.getText().isEmpty());
		res = res && (!username.getText().isEmpty());
		res = res && (!email.getText().isEmpty());
		res = res && (!address.getText().isEmpty());
		res = res && (!phone.getText().isEmpty());
		return res;
	}
}
