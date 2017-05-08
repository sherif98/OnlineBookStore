package application;

import java.sql.Date;
import java.sql.SQLException;

import application.db.entities.Author;
import application.db.entities.Book;
import application.db.entities.Cart;
import application.db.entities.Category;
import application.db.entities.Publisher;
import application.db.repositories.CartRepo;
import application.db.repositories.CustomerRepo;
import application.db.repositories.SoldRepo;

public class TestBookRepo {
	public static void main(String[] args) throws SQLException {
		Book book = new Book();
		Category category = new Category();
		category.setId(1);
		category.setType("Scince");
		
		Publisher publisher = new Publisher();
		publisher.setPublisherName("hamada");
		publisher.setPhone("123455");
		publisher.setAddress("address");
		
		
		book.setISBN(123);
		book.setTitle("introduction");
		book.setQuantity(100);
		book.setThreshold(20);
		book.setPublicationYear(new Date(1000));
		book.setSellingPrice(10.0);
		book.setCategoryId(1);
		book.setPublisherName("hamada");

		Author author = new Author();
		author.setName("sherif");
		author.setISBN(123);
		
		Cart cart = new Cart();
		cart.setCustomerId(3);
		cart.setISBN(123);
		cart.setQuantity(10);
		
		CartRepo cartRepo = CartRepo.getInstance();
		SoldRepo soldRepo = SoldRepo.getInstance();
		
		CustomerRepo customerRepo = CustomerRepo.getInstance();
		customerRepo.promot(3);;
	}
}
