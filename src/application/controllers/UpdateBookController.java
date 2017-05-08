package application.controllers;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

public class UpdateBookController implements CustomerHolder {

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

	public void setBook(Book bookToUpdate) {
		if (bookToUpdate != null) {
			isbnTextField.setText(String.valueOf(bookToUpdate.getISBN()));
			thresHoldTextField.setText(String.valueOf(bookToUpdate.getThreshold()));
			titleTextField.setText(bookToUpdate.getTitle());
			publisherNameTextField.setText(bookToUpdate.getPublisherName());
			String category = getCategory(bookToUpdate.getCategoryId());
			categoryTextField.setText(category);
			String authors = getAuthors(bookToUpdate.getISBN());
			authorsTextField.setText(authors);
			priceTextField.setText(String.valueOf(bookToUpdate.getSellingPrice()));
			datePicker.setValue(bookToUpdate.getPublicationYear().toLocalDate());
			quantityTextField.setText(String.valueOf(bookToUpdate.getQuantity()));
		}
	}

	private String getAuthors(int isbn) {
		List<Author> bookAuthors = new ArrayList<>();
		try {
			bookAuthors = authorRepo.getBookAuthors(isbn);
		} catch (SQLException e) {
			System.out.println("Book Authors Not Found with isbn = " + isbn);
			e.printStackTrace();
		}
		StringBuilder b = new StringBuilder();
		bookAuthors.forEach(author -> b.append(author.getName()).append(","));
		return b.toString();
	}

	private String getCategory(int categoryId) {
		try {
			Category category = categoryRepo.selectById(categoryId);
			return category.getType();
		} catch (SQLException e) {
			System.out.println("Cant find Category with id = " + categoryId);
			e.printStackTrace();
		}
		System.out.println("error happend In getCategory()");
		return "";
	}

	public void updateBook() {
		if (!checkArguments()) {
			warningLabel.setText("*Please fill All Fields");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			Book book = new Book();
			// TODO how to update authors
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
			bookRepo.update(book);
		} catch (SQLException e) {
			warningLabel.setText("Cant Update Book Check All Fields");
			warningLabel.setVisible(true);
			System.out.println("Failed to update Book");
			e.printStackTrace();
			return;
		}
		switchToCartScene();
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
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.CART_SCENE.getPath()));
			Parent root = loader.load();
			CartController cartController = loader.getController();
			cartController.setCustomer(customer);
			Main.loadScene(root, Scene.CART_SCENE.getWidth(), Scene.CART_SCENE.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
