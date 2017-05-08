package application.db.entities;

import java.sql.Date;

public class Book {
	private int ISBN;
	private String title;
	private int quantity;
	private int threshold;
	private Date publicationYear;
	private double sellingPrice;
	private int categoryId;
	private String publisherName;

	public int getISBN() {
		return ISBN;
	}

	public void setISBN(int iSBN) {
		ISBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Date getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Date publicationYear) {
		this.publicationYear = publicationYear;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [ISBN=");
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
		builder.append(", categoryId=");
		builder.append(categoryId);
		builder.append(", publisherName=");
		builder.append(publisherName);
		builder.append("]");
		return builder.toString();
	}

}
