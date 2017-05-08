package application.controllers;

import application.Main;
import application.db.entities.Customer;
import application.db.repositories.CustomerRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	private static final Scene CART_SCENE = Scene.CART_SCENE;
	private static final Scene SIGN_UP_SCENE = Scene.SIGN_UP_SCENE;

	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private Label warningLabel;

	private CustomerRepo customerRepo = CustomerRepo.getInstance();

	public void login() {
		if (!checkAguemtns()) {
			warningLabel.setText("*Please fill all parameters");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			String username = usernameTextField.getText();
			String password = passwordTextField.getText();
			Customer customer = customerRepo.selectByUsernameAndPassword(username, password);
			if (customer == null) {
				warningLabel.setText("*User Not Found in database");
				warningLabel.setVisible(true);
				return;
			}
			System.out.println(customer);
			FXMLLoader loader = new FXMLLoader(getClass().getResource(CART_SCENE.getPath()));
			Parent root = loader.load();
			CartController cartController = loader.getController();
			cartController.setCustomer(customer);
			Main.loadScene(root, CART_SCENE.getWidth(), CART_SCENE.getHeight());
		} catch (Exception e) {
			System.out.println("LoginController: Error happend in login method: ");
			e.printStackTrace();
		}
	}

	public void signUp() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(SIGN_UP_SCENE.getPath()));
		try {
			Main.loadScene(loader.load(), SIGN_UP_SCENE.getWidth(), SIGN_UP_SCENE.getHeight());
		} catch (Exception e) {
			System.out.println("Error in Loading sign up scene");
			e.printStackTrace();
		}
	}

	private boolean checkAguemtns() {
		boolean res = true;
		res = res && (!usernameTextField.getText().isEmpty());
		res = res && (!passwordTextField.getText().isEmpty());
		return res;
	}
}
