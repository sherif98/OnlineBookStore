package application;

import java.sql.SQLException;

import application.db.entities.Cart;
import application.db.repositories.CartRepo;

public class TestCustomerRepo {
	public static void main(String[] args) throws SQLException {
		CartRepo repo = CartRepo.getInstance();
		for (int i = 0; i < 100; ++i) {
			Cart cart = new Cart();
			cart.setCustomerId(3);
			cart.setISBN(12);
			cart.setQuantity(i);
			cart.setSellingPrice(i);
			repo.persist(cart);
		}
	}
}
