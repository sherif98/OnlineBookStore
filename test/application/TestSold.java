package application;

import java.sql.Date;
import java.sql.SQLException;

import application.db.entities.Sold;
import application.db.repositories.SoldRepo;

public class TestSold {
	public static void main(String[] args) throws SQLException {
		SoldRepo soldRepo = SoldRepo.getInstance();
		Sold sold = new Sold();
		sold.setCustomerId(3);
		sold.setISBN(12);
		sold.setSellingPrice(100);
		sold.setSellingDate(new Date(1000));
		soldRepo.persist(sold);
	}
}
