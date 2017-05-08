package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Book;

public class BookRepo {
	private Repo repo = Repo.getInstance();
	private static BookRepo instance = new BookRepo();
	private static final String SELECT_BY_ISBN_TEMPLATE = "select * from book where ISBN = ?";
	private static final String SELECT_BY_TITLE_TEMPLATE = "select * from book where title = ?";
	private static final String SELECT_BY_CATEGORY_TEMPLATE = "select * from book where book.category_id = ?";
	private static final String SELECT_BY_PUBLISHER_NAME_TEMPLATE = "{call select_by_publisher_name(?)}";
	private static final String SELECT_BY_AUTHOR_TEMPLATE = "{call select_by_author(?)}";
	private static final String INSERT_STATEMENT = "insert into book values (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_STATEMENT = "update book set title = ?, quantity = ?, threshold = ?, publication_year = ?, selling_price = ?, category_id = ?, publisher_name = ? where ISBN = ?";
	private static final String SELECT_ALL_TEMPLATE = "select * from book limit 20";
	private static final String SELECT_WITH_PAGING_TEMPLATE = "select * from book limit ? offset ?";

	private PreparedStatement selectByISBN;
	private PreparedStatement selectByTitle;
	private PreparedStatement selectByCategory;
	private PreparedStatement selectByPublisherName;
	private PreparedStatement selectByAuthor;
	private PreparedStatement insert;
	private PreparedStatement update;
	private PreparedStatement selectAll;
	private PreparedStatement selectWithPaging;

	private BookRepo() {
		this.selectByISBN = repo.getPreparedStatement(SELECT_BY_ISBN_TEMPLATE);
		this.selectByTitle = repo.getPreparedStatement(SELECT_BY_TITLE_TEMPLATE);
		this.selectByCategory = repo.getPreparedStatement(SELECT_BY_CATEGORY_TEMPLATE);
		this.selectByPublisherName = repo.getCallableStatement(SELECT_BY_PUBLISHER_NAME_TEMPLATE);
		this.selectByAuthor = repo.getCallableStatement(SELECT_BY_AUTHOR_TEMPLATE);
		this.insert = repo.getPreparedStatement(INSERT_STATEMENT);
		this.update = repo.getPreparedStatement(UPDATE_STATEMENT);
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.selectWithPaging = repo.getPreparedStatement(SELECT_WITH_PAGING_TEMPLATE);
	}

	public static BookRepo getInstance() {
		return instance;
	}

	public Book selectByISBN(int ISBN) throws SQLException {
		this.selectByISBN.setInt(1, ISBN);
		ResultSet resultSet = this.selectByISBN.executeQuery();
		resultSet.next();
		return this.buildBook(resultSet);
	}

	public List<Book> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<Book> bookList = new ArrayList<>();
		while (resultSet.next()) {
			bookList.add(this.buildBook(resultSet));
		}
		return bookList;
	}

	public List<Book> selectWithPaging(int pageNumber, int pageSize) throws SQLException {
		int beginIndex = pageNumber * pageSize;
		selectWithPaging.setInt(1, pageSize);
		selectWithPaging.setInt(2, beginIndex);
		ResultSet resultSet = selectWithPaging.executeQuery();
		List<Book> bookList = new ArrayList<>();
		while (resultSet.next()) {
			bookList.add(this.buildBook(resultSet));
		}
		return bookList;
	}

	public List<Book> selectByTitle(String title) throws SQLException {
		this.selectByTitle.setString(1, title);
		ResultSet resultSet = this.selectByTitle.executeQuery();
		List<Book> bookList = new ArrayList<>();
		while (resultSet.next()) {
			bookList.add(this.buildBook(resultSet));
		}
		return bookList;
	}

	public void persist(Book book) throws SQLException {
		this.insert.setInt(1, book.getISBN());
		this.insert.setString(2, book.getTitle());
		this.insert.setInt(3, book.getQuantity());
		this.insert.setInt(4, book.getThreshold());
		this.insert.setDate(5, book.getPublicationYear());
		this.insert.setDouble(6, book.getSellingPrice());
		this.insert.setInt(7, book.getCategoryId());
		this.insert.setString(8, book.getPublisherName());
		this.insert.executeUpdate();
	}

	public void update(Book book) throws SQLException {
		this.update.setString(1, book.getTitle());
		this.update.setInt(2, book.getQuantity());
		this.update.setInt(3, book.getThreshold());
		this.update.setDate(4, book.getPublicationYear());
		this.update.setDouble(5, book.getSellingPrice());
		this.update.setInt(6, book.getCategoryId());
		this.update.setString(7, book.getPublisherName());
		this.update.setInt(8, book.getISBN());
		this.update.executeUpdate();
	}

	public List<Book> selectByCategory(int categoryId) throws SQLException {
		this.selectByCategory.setInt(1, categoryId);
		ResultSet resultSet = this.selectByCategory.executeQuery();
		List<Book> bookList = new ArrayList<>();
		while (resultSet.next()) {
			bookList.add(this.buildBook(resultSet));
		}
		return bookList;
	}

	public List<Book> selectByPublisherName(String publisherName) throws SQLException {
		this.selectByPublisherName.setString(1, publisherName);
		ResultSet resultSet = this.selectByPublisherName.executeQuery();
		List<Book> bookList = new ArrayList<>();
		while (resultSet.next()) {
			bookList.add(this.buildBook(resultSet));
		}
		return bookList;
	}

	public List<Book> selectByAuthor(String authorName) throws SQLException {
		this.selectByAuthor.setString(1, authorName);
		ResultSet resultSet = this.selectByAuthor.executeQuery();
		List<Book> bookList = new ArrayList<>();
		while (resultSet.next()) {
			bookList.add(this.buildBook(resultSet));
		}
		return bookList;
	}

	private Book buildBook(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			return null;
		}
		Book book = new Book();
		book.setISBN(resultSet.getInt("ISBN"));
		book.setTitle(resultSet.getString("title"));
		book.setQuantity(resultSet.getInt("quantity"));
		book.setThreshold(resultSet.getInt("threshold"));
		book.setPublicationYear(resultSet.getDate("publication_year"));
		book.setSellingPrice(resultSet.getDouble("selling_price"));
		book.setCategoryId(resultSet.getInt("category_id"));
		book.setPublisherName(resultSet.getString("publisher_name"));
		return book;
	}
}
