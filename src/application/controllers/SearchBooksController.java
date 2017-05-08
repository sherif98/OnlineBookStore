package application.controllers;

import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.db.entities.Book;
import application.db.entities.Category;
import application.db.entities.Customer;
import application.db.repositories.BookRepo;
import application.db.repositories.CartRepo;
import application.db.repositories.CategoryRepo;
import application.dto.BookDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchBooksController implements CustomerHolder {
	@FXML
	private TableView<BookDTO> bookTableView;
	@FXML
	private Label ISBNLabel;
	@FXML
	private TextField ISBNTextField;
	@FXML
	private TextField quantityTextField;
	@FXML
	private TextField ISBNModifyTextField;
	@FXML
	private Button modifyBookButton;
	@FXML
	private Label warningLabel;
	@FXML
	private ChoiceBox<SearchItem> searchChoiceBox;
	@FXML
	private TextField searchTextField;

	private Customer customer;
	private BookRepo bookRepo = BookRepo.getInstance();
	private CategoryRepo categoryRepo = CategoryRepo.getInstance();
	private CartRepo cartRepo = CartRepo.getInstance();

	@FXML
	public void initialize() {
		ObservableList<TableColumn<BookDTO, ?>> columns = bookTableView.getColumns();
		columns.get(0).setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		columns.get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
		columns.get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
		columns.get(3).setCellValueFactory(new PropertyValueFactory<>("publisherName"));
		columns.get(4).setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
		columns.get(5).setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
		columns.get(6).setCellValueFactory(new PropertyValueFactory<>("category"));
		reloadBooks();
		loadChoiceBox();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		if (customer != null) {
			modifyBookButton.setVisible(customer.isManager());
			ISBNLabel.setVisible(customer.isManager());
			ISBNModifyTextField.setVisible(customer.isManager());
		}
	}

	private void loadChoiceBox() {
		ObservableList<SearchItem> searchChoiceBoxItems = FXCollections.observableArrayList(SearchItem.values());
		searchChoiceBox.setItems(searchChoiceBoxItems);
	}

	public void reloadBooks() {
		try {
			warningLabel.setVisible(false);
			List<Book> books = bookRepo.selectAll();
			bookTableView.getItems().clear();
			for (Book book : books) {
				Category category = categoryRepo.selectById(book.getCategoryId());
				BookDTO bookDTO = new BookDTO(book, category.getType());
				bookTableView.getItems().add(bookDTO);
			}
		} catch (Exception e) {
			warningLabel.setText("*Error in loading books");
			warningLabel.setVisible(true);
			System.out.println("Search Book controllers: Error in reload books");
			e.printStackTrace();
		}
	}

	public void search() {
		if (!checkSearchArgs()) {
			warningLabel.setText("*Please fill all fields");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			SingleSelectionModel<SearchItem> selection = searchChoiceBox.getSelectionModel();
			SearchItem selectedItem = selection.getSelectedItem();
			List<Book> books;
			switch (selectedItem) {
			case CATEGORY:
				Category categoryId = categoryRepo.selectByCategoryType(searchTextField.getText());
				books = bookRepo.selectByCategory(categoryId.getId());
				reloadBooks(books);
				break;
			case ISBN:
				int bookISBN = Integer.parseInt(searchTextField.getText());
				Book book = bookRepo.selectByISBN(bookISBN);
				books = new ArrayList<>();
				books.add(book);
				reloadBooks(books);
				break;
			case AUTHOR:
				books = bookRepo.selectByAuthor(searchTextField.getText());
				reloadBooks(books);
				break;
			case PUBLISHER:
				books = bookRepo.selectByPublisherName(searchTextField.getText());
				reloadBooks(books);
				break;
			}
		} catch (Exception e) {
			warningLabel.setText("*Error happend will loading books");
			warningLabel.setVisible(true);
			System.out.println("*Error in Search books controller search");
			e.printStackTrace();
		}
	}

	public void addToCart() {
		if (!checkAddToCartArgs()) {
			warningLabel.setText("*Please fill all fields");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			int ISBN = Integer.parseInt(ISBNTextField.getText());
			int quantity = Integer.parseInt(quantityTextField.getText());
			cartRepo.buyBook(customer.getId(), ISBN, quantity);
		} catch (Exception e) {
			warningLabel.setText("*Error happend will adding to cart");
			warningLabel.setVisible(true);
			e.printStackTrace();
		}
	}

	public void switchToUpdateBookScene() {
		if (!checkSwitchToUpdateBookSceneArgs()) {
			warningLabel.setText("*Please enter valid ISBN");
			warningLabel.setVisible(true);
			return;
		}
		try {
			warningLabel.setVisible(false);
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.UPDATE_BOOK_SCENE.getPath()));
			Parent root = loader.load();
			UpdateBookController updateBookController = loader.getController();
			int ISBN = Integer.parseInt(ISBNModifyTextField.getText());
			Book book = bookRepo.selectByISBN(ISBN);
			updateBookController.setCustomer(customer);
			System.out.println(book);
			updateBookController.setBook(book);
			Main.loadScene(root, Scene.UPDATE_BOOK_SCENE.getWidth(), Scene.UPDATE_BOOK_SCENE.getHeight());
		} catch (Exception e) {
			warningLabel.setText("*Error happend");
			warningLabel.setVisible(true);
			System.out.println("Error in switchtoUpdateMethod");
			e.printStackTrace();
		}
	}

	private boolean checkSwitchToUpdateBookSceneArgs() {
		if (ISBNModifyTextField.getText().isEmpty()) {
			return false;
		}
		int ISBN = Integer.parseInt(ISBNModifyTextField.getText());
		try {
			Book book = bookRepo.selectByISBN(ISBN);
			if (book == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean checkAddToCartArgs() {
		boolean res = true;
		res = res && (!ISBNTextField.getText().isEmpty());
		res = res && (!quantityTextField.getText().isEmpty());
		return res;
	}

	private void reloadBooks(List<Book> books) {
		try {
			bookTableView.getItems().clear();
			for (Book book : books) {
				Category category = categoryRepo.selectById(book.getCategoryId());
				BookDTO bookDTO = new BookDTO(book, category.getType());
				bookTableView.getItems().add(bookDTO);
			}
		} catch (Exception e) {
			warningLabel.setText("*Error happend will loading books");
			warningLabel.setVisible(true);
			System.out.println("SearchBook reloadBooks error");
			e.printStackTrace();
		}
	}

	private boolean checkSearchArgs() {
		boolean res = true;
		if (searchChoiceBox.getSelectionModel() == null || searchChoiceBox.getSelectionModel().isEmpty()) {
			return false;
		}
		if (searchChoiceBox.getSelectionModel().getSelectedItem() == null) {
			return false;
		}
		res = res && (!searchTextField.getText().isEmpty());
		return res;
	}

	public void switchToCartScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Scene.CART_SCENE.getPath()));
			Parent root = loader.load();
			CartController cartController = loader.getController();
			cartController.setCustomer(customer);
			Main.loadScene(root, Scene.CART_SCENE.getWidth(), Scene.CART_SCENE.getHeight());
		} catch (Exception e) {
			System.out.println("Error in Loading sign up scene");
			e.printStackTrace();
		}
	}
}
