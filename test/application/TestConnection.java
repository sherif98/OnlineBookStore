package application;

import java.sql.SQLException;

import application.db.entities.Customer;
import application.db.repositories.CustomerRepo;

public class TestConnection {
	public static void main(String[] args) throws SQLException {
		CustomerRepo customerRepo = CustomerRepo.getInstance();
//		Customer customer = new Customer();
//		customer.setEmail("hamada@gmail.com");
//		customer.setFirstname("hamada");
//		customer.setLastname("baba");
//		customer.setManager(false);
//		customer.setPassword("password");
//		customer.setUsername("hamadaUser");
//		customer.setPhone("1234");
//		customer.setShippingAddress("address,hamada");
//		customerRepo.persist(customer);
		
		customerRepo.promot(2);
		
		Customer customer = customerRepo.selectById(2);
		System.out.println(customer);
	}
}
