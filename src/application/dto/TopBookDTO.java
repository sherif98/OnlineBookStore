package application.dto;

import application.db.entities.TopBook;
import javafx.beans.property.SimpleStringProperty;

public class TopBookDTO {
	private final SimpleStringProperty ISBN;
	private final SimpleStringProperty title;
	private final SimpleStringProperty publisherName;
	private final SimpleStringProperty soldCopies;
	
	public TopBookDTO(TopBook book) {
		this.ISBN = new SimpleStringProperty(String.valueOf(book.getISBN()));
		this.title = new SimpleStringProperty(book.getTitle());
		this.publisherName = new SimpleStringProperty(book.getPublisherName());
		this.soldCopies = new SimpleStringProperty(String.valueOf(book.getSoldCopies()));
	}

	public String getISBN() {
		return ISBN.get();
	}

	public String getTitle() {
		return title.get();
	}

	public String getPublisherName() {
		return publisherName.get();
	}

	public String getSoldCopies() {
		return soldCopies.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TopBookDTO [ISBN=");
		builder.append(ISBN);
		builder.append(", title=");
		builder.append(title);
		builder.append(", publisherName=");
		builder.append(publisherName);
		builder.append(", soldCopies=");
		builder.append(soldCopies);
		builder.append("]");
		return builder.toString();
	}
	
}
