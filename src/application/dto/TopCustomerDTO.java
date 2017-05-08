package application.dto;

import application.db.entities.TopCustomer;
import javafx.beans.property.SimpleStringProperty;

public class TopCustomerDTO {
	private final SimpleStringProperty id;
	private final SimpleStringProperty firstname;
	private final SimpleStringProperty lastname;
	private final SimpleStringProperty totalCash;

	public TopCustomerDTO(TopCustomer customer) {
		this.id = new SimpleStringProperty(String.valueOf(customer.getId()));
		this.firstname = new SimpleStringProperty(customer.getFirstname());
		this.lastname = new SimpleStringProperty(customer.getLastname());
		this.totalCash = new SimpleStringProperty(String.valueOf(customer.getTotalCash()));
	}

	public String getId() {
		return id.get();
	}

	public String getFirstname() {
		return firstname.get();
	}

	public String getLastname() {
		return lastname.get();
	}

	public String getTotalCash() {
		return totalCash.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TopCustomerDTO [id=");
		builder.append(id);
		builder.append(", firstname=");
		builder.append(firstname);
		builder.append(", lastname=");
		builder.append(lastname);
		builder.append(", totalCash=");
		builder.append(totalCash);
		builder.append("]");
		return builder.toString();
	}

}
