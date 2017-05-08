package application.dto;

import application.db.entities.Customer;
import javafx.beans.property.SimpleStringProperty;

public class CustomerDTO {
	private final SimpleStringProperty id;
	private final SimpleStringProperty username;
	private final SimpleStringProperty password;
	private final SimpleStringProperty firstname;
	private final SimpleStringProperty lastname;
	private final SimpleStringProperty email;
	private final SimpleStringProperty phone;
	private final SimpleStringProperty shippingAddress;
	private final SimpleStringProperty isManager;

	public CustomerDTO(Customer customer) {
		this.id = new SimpleStringProperty(String.valueOf(customer.getId()));
		this.username = new SimpleStringProperty(customer.getUsername());
		this.password = new SimpleStringProperty(customer.getPassword());
		this.firstname = new SimpleStringProperty(customer.getFirstname());
		this.lastname = new SimpleStringProperty(customer.getLastname());
		this.email = new SimpleStringProperty(customer.getEmail());
		this.phone = new SimpleStringProperty(customer.getPhone());
		this.shippingAddress = new SimpleStringProperty(customer.getShippingAddress());
		this.isManager = new SimpleStringProperty(String.valueOf(customer.isManager()));
	}

	public String getId() {
		return id.get();
	}

	public String getUsername() {
		return username.get();
	}

	public String getPassword() {
		return password.get();
	}

	public String getFirstname() {
		return firstname.get();
	}

	public String getLastname() {
		return lastname.get();
	}

	public String getEmail() {
		return email.get();
	}

	public String getPhone() {
		return phone.get();
	}

	public String getShippingAddress() {
		return shippingAddress.get();
	}

	public String getIsManager() {
		return isManager.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerDTO [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", firstname=");
		builder.append(firstname);
		builder.append(", lastname=");
		builder.append(lastname);
		builder.append(", email=");
		builder.append(email);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", shippingAddress=");
		builder.append(shippingAddress);
		builder.append(", isManager=");
		builder.append(isManager);
		builder.append("]");
		return builder.toString();
	}

}
