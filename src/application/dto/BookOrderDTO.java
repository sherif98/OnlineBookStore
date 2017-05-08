package application.dto;

import application.db.entities.BookOrder;
import javafx.beans.property.SimpleStringProperty;

public class BookOrderDTO {
	private final SimpleStringProperty id;
	private final SimpleStringProperty publisherName;
	private final SimpleStringProperty ISBN;
	private final SimpleStringProperty quantity;

	public BookOrderDTO(BookOrder bookOrder) {
		this.id = new SimpleStringProperty(String.valueOf(bookOrder.getId()));
		this.publisherName = new SimpleStringProperty(bookOrder.getPublisherName());
		this.ISBN = new SimpleStringProperty(String.valueOf(bookOrder.getISBN()));
		this.quantity = new SimpleStringProperty(String.valueOf(bookOrder.getQuantity()));
	}

	public String getId() {
		return id.get();
	}

	public String getPublisherName() {
		return publisherName.get();
	}

	public String getISBN() {
		return ISBN.get();
	}

	public String getQuantity() {
		return quantity.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookOrderDTO [id=");
		builder.append(id);
		builder.append(", publisherName=");
		builder.append(publisherName);
		builder.append(", ISBN=");
		builder.append(ISBN);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append("]");
		return builder.toString();
	}
}
