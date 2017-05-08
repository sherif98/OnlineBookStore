package application.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import application.Main;
import application.db.entities.Author;
import application.db.entities.Book;
import application.db.entities.Category;
import application.db.entities.Customer;
import application.db.repositories.AuthorRepo;
import application.db.repositories.BookRepo;
import application.db.repositories.CategoryRepo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddBookController implements CustomerHolder {
	@FXML
	private TextField isbnTextField;
	@FXML
	private TextField thresHoldTextField;
	@FXML
	private TextField titleTextField;
	@FXML
	private TextField publisherNameTextField;
	@FXML
	private TextField categoryTextField;
	@FXML
	private TextField authorsTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TextField quantityTextField;

	@FXML
	private Label warningLabel;

	private CategoryRepo categoryRepo = CategoryRepo.getInstance();
	private BookRepo bookRepo = BookRepo.getInstance();
	private AuthorRepo authorRepo = AuthorRepo.getInstance();

	private Customer customer;

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void addBook() {
		if (!checkArguments()) {
			warningLabel.setText("*Please fill All Fields");
			warningLabel.setVisible(true);
			return;
		}
		warningLabel.setVisible(false);
		try {
			Book book = new Book();
			book.setISBN(Integer.parseInt(isbnTextField.getText()));
			book.setTitle(titleTextField.getText());
			book.setQuantity(Integer.parseInt(quantityTextField.getText()));
			book.setThreshold(Integer.parseInt(thresHoldTextField.getText()));
			LocalDate localDate = datePicker.getValue();
			Date date = java.sql.Date.valueOf(localDate);
			book.setPublicationYear(date);
			book.setSellingPrice(Double.parseDouble(priceTextField.getText()));
			int id = getCategoryId(categoryTextField.getText());
			book.setCategoryId(id);
			book.setPublisherName(publisherNameTextField.getText());

			bookRepo.persist(book);
			List<Author> authors = getBookAuthors(book.getISBN());
			authors.forEach(a -> {
				try {
					authorRepo.persist(a);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} catch (SQLException e) {
			warningLabel.setText("Can't Insert Book Please Re Check Fields");
			warningLabel.setVisible(true);
			System.out.println("Failed to update Book");
			e.printStackTrace();
			return;
		}

		switchToCartScene();
	}

	private List<Author> getBookAuthors(int isbn) {
		String[] authorNames = authorsTextField.getText().split(",");
		List<Author> bookAuthors = Arrays.stream(authorNames).map(s -> {
			Author a = new Author();
			a.setISBN(isbn);
			a.setName(s.trim());
			return a;
		}).collect(Collectors.toList());
		return bookAuthors;
	}

	private int getCategoryId(String categoryType) {
		Category category = null;
		try {
			category = categoryRepo.selectByCategoryType(categoryType);
		} catch (SQLException e) {
			warningLabel.setText("No Category with name = " + categoryType);
			warningLabel.setVisible(true);
			System.out.println("No Category with name = " + categoryType);
			e.printStackTrace();
		}
		return category.getId();
	}

	private boolean checkArguments() {
		boolean res = true;
		res = res && !isbnTextField.getText().isEmpty();
		res = res && !thresHoldTextField.getText().isEmpty();
		res = res && !titleTextField.getText().isEmpty();
		res = res && !publisherNameTextField.getText().isEmpty();
		res = res && !categoryTextField.getText().isEmpty();
		res = res && !authorsTextField.getText().isEmpty();
		res = res && !priceTextField.getText().isEmpty();
		res = res && datePicker.getValue() != null;
		res = res && !quantityTextField.getText().isEmpty();
		return res;
	}

	public void back() {
		switchToCartScene();
	}

	private void switchToCartScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.CART_SCENE.getPath()));
		Parent root = null;
		try {
			root = loader.load();
			CartController cartController = loader.getController();
			cartController.setCustomer(customer);
			Main.loadScene(root, Scene.CART_SCENE.getWidth(), Scene.CART_SCENE.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
