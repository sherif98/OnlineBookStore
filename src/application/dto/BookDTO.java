package application.dto;

import application.db.entities.Book;
import javafx.beans.property.SimpleStringProperty;

public class BookDTO {
	private final SimpleStringProperty ISBN;
	private final SimpleStringProperty title;
	private final SimpleStringProperty quantity;
	private final SimpleStringProperty threshold;
	private final SimpleStringProperty publicationYear;
	private final SimpleStringProperty sellingPrice;
	private final SimpleStringProperty category;
	private final SimpleStringProperty publisherName;

	public BookDTO(Book book, String category) {
		this.ISBN = new SimpleStringProperty(String.valueOf(book.getISBN()));
		this.title = new SimpleStringProperty(book.getTitle());
		this.quantity = new SimpleStringProperty(String.valueOf(book.getQuantity()));
		this.threshold = new SimpleStringProperty(String.valueOf(book.getThreshold()));
		this.publicationYear = new SimpleStringProperty(book.getPublicationYear().toString());
		this.sellingPrice = new SimpleStringProperty(String.valueOf(book.getSellingPrice()));
		this.category = new SimpleStringProperty(category);
		this.publisherName = new SimpleStringProperty(book.getPublisherName());
	}

	public String getISBN() {
		return ISBN.get();
	}

	public String getTitle() {
		return title.get();
	}

	public String getQuantity() {
		return quantity.get();
	}

	public String getThreshold() {
		return threshold.get();
	}

	public String getPublicationYear() {
		return publicationYear.get();
	}

	public String getSellingPrice() {
		return sellingPrice.get();
	}

	public String getCategory() {
		return category.get();
	}

	public String getPublisherName() {
		return publisherName.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookDTO [ISBN=");
		builder.append(ISBN);
		builder.append(", title=");
		builder.append(title);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", threshold=");
		builder.append(threshold);
		builder.append(", publicationYear=");
		builder.append(publicationYear);
		builder.append(", sellingPrice=");
		builder.append(sellingPrice);
		builder.append(", category=");
		builder.append(category);
		builder.append(", publisherName=");
		builder.append(publisherName);
		builder.append("]");
		return builder.toString();
	}

}
