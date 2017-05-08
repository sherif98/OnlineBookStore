package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.BookOrder;

public class BookOrderRepo {
	private Repo repo = Repo.getInstance();
	private static BookOrderRepo instance = new BookOrderRepo();
	private static final String SELECT_ALL_TEMPLATE = "select * from book_order";
	private static final String SELECT_WITH_PAGIN = "select * from book_order limit ? offset ?";
	private static final String INSERT_TEMPLATE = "insert into book_order values(?, ?, ?, ?)";
	private static final String UPDATE_TEMPLATE = "update book_order set publisher_name = ?, ISBN = ?, quantity = ? where id = ?";
	private static final String DELETE_BY_ID_TEMPLATE = "delete from book_order where id = ?";

	private PreparedStatement selectAll;
	private PreparedStatement selectWithPaging;
	private PreparedStatement insert;
	private PreparedStatement update;
	private PreparedStatement deleteById;

	private BookOrderRepo() {
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.selectWithPaging = repo.getPreparedStatement(SELECT_WITH_PAGIN);
		this.insert = repo.getPreparedStatement(INSERT_TEMPLATE);
		this.update = repo.getPreparedStatement(UPDATE_TEMPLATE);
		this.deleteById = repo.getPreparedStatement(DELETE_BY_ID_TEMPLATE);
	}

	public void deleteById(int orderId) throws SQLException {
		this.deleteById.setInt(1, orderId);
		this.deleteById.executeUpdate();
	}

	public static BookOrderRepo getInstance() {
		return instance;
	}

	public List<BookOrder> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<BookOrder> orderList = new ArrayList<>();
		while (resultSet.next()) {
			orderList.add(this.buildBookOrder(resultSet));
		}
		return orderList;
	}

	public List<BookOrder> selectWithPaging(int pageNumber, int pageSize) throws SQLException {
		int beginIndex = pageNumber * pageSize;
		selectWithPaging.setInt(1, pageSize);
		selectWithPaging.setInt(2, beginIndex);
		ResultSet resultSet = selectWithPaging.executeQuery();
		List<BookOrder> orderList = new ArrayList<>();
		while (resultSet.next()) {
			orderList.add(this.buildBookOrder(resultSet));
		}
		return orderList;
	}

	public void update(BookOrder bookOrder) throws SQLException {
		this.update.setString(1, bookOrder.getPublisherName());
		this.update.setInt(2, bookOrder.getISBN());
		this.update.setInt(3, bookOrder.getQuantity());
		this.update.setInt(4, bookOrder.getId());
		this.update.executeUpdate();
	}

	private BookOrder buildBookOrder(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			return null;
		}
		BookOrder bookOrder = new BookOrder();
		bookOrder.setId(resultSet.getInt("id"));
		bookOrder.setPublisherName(resultSet.getString("publisher_name"));
		bookOrder.setISBN(resultSet.getInt("ISBN"));
		bookOrder.setQuantity(resultSet.getInt("quantity"));
		return bookOrder;
	}

	public void persist(BookOrder bookOrder) throws SQLException {
		this.insert.setInt(1, bookOrder.getId());
		this.insert.setString(2, bookOrder.getPublisherName());
		this.insert.setInt(3, bookOrder.getISBN());
		this.insert.setInt(4, bookOrder.getQuantity());
		this.insert.executeUpdate();
	}

}
