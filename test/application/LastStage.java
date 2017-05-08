package application;

import java.sql.Date;
import java.sql.SQLException;

import application.db.entities.Book;
import application.db.repositories.BookRepo;

public class LastStage {
	public static void main(String[] args) throws SQLException {
		BookRepo bookRepo = BookRepo.getInstance();

		for (int i = 0; i < 1000; i++) {
			Book book = new Book();
			book.setISBN(i + 3);
			book.setTitle("introduction " + i);
			book.setQuantity(100);
			book.setThreshold(20);
			book.setPublicationYear(new Date(1000L));
			book.setSellingPrice(5 * (i + 1));
			book.setCategoryId((i % 5) + 1);
			book.setPublisherName("hamada");
			bookRepo.persist(book);
		}
	}
}
