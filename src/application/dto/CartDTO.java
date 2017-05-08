package application.dto;

import application.db.entities.Cart;
import javafx.beans.property.SimpleStringProperty;

public class CartDTO {
	private final SimpleStringProperty id;
	private final SimpleStringProperty customerId;
	private final SimpleStringProperty ISBN;
	private final SimpleStringProperty quantity;
	private final SimpleStringProperty sellingPrice;

	public CartDTO(Cart cart) {
		this.id = new SimpleStringProperty(String.valueOf(cart.getId()));
		this.customerId = new SimpleStringProperty(String.valueOf(cart.getCustomerId()));
		this.ISBN = new SimpleStringProperty(String.valueOf(cart.getISBN()));
		this.quantity = new SimpleStringProperty(String.valueOf(cart.getQuantity()));
		this.sellingPrice = new SimpleStringProperty(String.valueOf(cart.getSellingPrice()));
	}

	public String getId() {
		return id.get();
	}

	public String getCustomerId() {
		return customerId.get();
	}

	public String getISBN() {
		return ISBN.get();
	}

	public String getQuantity() {
		return quantity.get();
	}

	public String getSellingPrice() {
		return sellingPrice.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CartDTO [id=");
		builder.append(id);
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append(", ISBN=");
		builder.append(ISBN);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", sellingPrice=");
		builder.append(sellingPrice);
		builder.append("]");
		return builder.toString();
	}

}
