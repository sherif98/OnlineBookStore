package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Sold;
import application.db.entities.TopBook;
import application.db.entities.TopCustomer;

public class SoldRepo {
	private Repo repo = Repo.getInstance();
	private static final String TOP_FIVE_CUSTOMERS_PATH = "D:/TopFiveCustomers.pdf";
	private static final String TOP_TEN_BOOKS_PATH = "D:/TopTenBooks.pdf";
	private static final String SELECT_BY_CUSTOMER_ID_TEMPLATE = "select * from sold where sold.customer_id = ?";
	private static final String SELECT_ALL_TEMPLATE = "select * from sold";
	private static final String SELECT_TOP_FIVE_TEMPLATE = "{call top_five_customers()}";
	private static final String SELECT_TOP_TEN_BOOKS_TEMPLATE = "{call top_ten_selling_books()}";
	private static final String SELECT_LAST_MONTH_PROFIT_TEMPLATE = "{call last_month_profit()}";
	private static final String INSERT_TEMPLATE = "insert into sold (customer_id, ISBN, quantity, selling_price, selling_date) values(?, ?, ?, ?, ?)";
	private static final String UPDATE_TEMPLATE = "update sold set customer_id = ?, ISBN = ?, quantity = ?, selling_price = ?, selling_date = ? where id = ?";

	private PreparedStatement selectByCustomerId;
	private PreparedStatement selectAll;
	private PreparedStatement selectTopFiveCostumer;
	private PreparedStatement selectTopTenBooks;
	private PreparedStatement selectLastMonthProfit;
	private PreparedStatement insert;
	private PreparedStatement update;

	private ReportGenerator reportGenerator = ReportGenerator.getInstance();

	private static SoldRepo instance = new SoldRepo();

	private SoldRepo() {
		this.selectByCustomerId = repo.getPreparedStatement(SELECT_BY_CUSTOMER_ID_TEMPLATE);
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.selectTopFiveCostumer = repo.getCallableStatement(SELECT_TOP_FIVE_TEMPLATE);
		this.selectTopTenBooks = repo.getCallableStatement(SELECT_TOP_TEN_BOOKS_TEMPLATE);
		this.selectLastMonthProfit = repo.getCallableStatement(SELECT_LAST_MONTH_PROFIT_TEMPLATE);
		this.insert = repo.getPreparedStatement(INSERT_TEMPLATE);
		this.update = repo.getPreparedStatement(UPDATE_TEMPLATE);
	}

	public void update(Sold sold) throws SQLException {
		this.update.setInt(1, sold.getCustomerId());
		this.update.setInt(2, sold.getISBN());
		this.update.setInt(3, sold.getQuantity());
		this.update.setDouble(4, sold.getSellingPrice());
		this.update.setDate(5, sold.getSellingDate());
		this.update.setInt(6, sold.getId());
		this.update.executeUpdate();
	}

	public void persist(Sold sold) throws SQLException {
		this.insert.setInt(1, sold.getCustomerId());
		this.insert.setInt(2, sold.getISBN());
		this.insert.setInt(3, sold.getQuantity());
		this.insert.setDouble(4, sold.getSellingPrice());
		this.insert.setDate(5, sold.getSellingDate());
		System.out.println(sold);
		System.out.println(this.insert);
		this.insert.executeUpdate();
	}

	public static SoldRepo getInstance() {
		return instance;
	}

	public List<Sold> selectByCustomerId(int customerId) throws SQLException {
		this.selectByCustomerId.setInt(1, customerId);
		ResultSet resultSet = this.selectByCustomerId.executeQuery();
		List<Sold> soldList = new ArrayList<>();
		while (resultSet.next()) {
			soldList.add(this.buildSold(resultSet));
		}
		return soldList;
	}

	public List<Sold> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<Sold> soldList = new ArrayList<>();
		while (resultSet.next()) {
			soldList.add(this.buildSold(resultSet));
		}
		return soldList;
	}

	public List<TopCustomer> selectTopFiveCustomers() throws SQLException {
		ResultSet resultSet = this.selectTopFiveCostumer.executeQuery();
		reportGenerator.generateTopFiveCustomersReport(resultSet, TOP_FIVE_CUSTOMERS_PATH);
		resultSet.beforeFirst();
		List<TopCustomer> topCustomerList = new ArrayList<>();
		while (resultSet.next()) {
			topCustomerList.add(this.buildTopCustomer(resultSet));
		}
		return topCustomerList;
	}

	public List<TopBook> selectTopTenBooks() throws SQLException {
		ResultSet resultSet = this.selectTopTenBooks.executeQuery();
		reportGenerator.generateTopTenBooks(resultSet, TOP_TEN_BOOKS_PATH);
		resultSet.beforeFirst();
		List<TopBook> topBookList = new ArrayList<>();
		while (resultSet.next()) {
			topBookList.add(this.buildTopBook(resultSet));
		}
		return topBookList;
	}

	private TopBook buildTopBook(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			return null;
		}
		TopBook book = new TopBook();
		book.setISBN(resultSet.getInt("ISBN"));
		book.setTitle(resultSet.getString("title"));
		book.setPublisherName(resultSet.getString("publisher_name"));
		book.setSoldCopies(resultSet.getInt("sold_copies"));
		return book;
	}

	public double getLastMonthProfit() throws SQLException {
		ResultSet resultSet = this.selectLastMonthProfit.executeQuery();
		if (resultSet.next()) {
			return resultSet.getDouble("profit");
		}
		return 0.0;
	}

	private Sold buildSold(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			return null;
		}
		Sold sold = new Sold();
		sold.setId(resultSet.getInt("id"));
		sold.setCustomerId(resultSet.getInt("customer_id"));
		sold.setISBN(resultSet.getInt("ISBN"));
		sold.setSellingPrice(resultSet.getDouble("selling_price"));
		sold.setQuantity(resultSet.getInt("quantity"));
		sold.setSellingDate(resultSet.getDate("selling_date"));
		return sold;
	}

	private TopCustomer buildTopCustomer(ResultSet resultSet) throws SQLException {
		if (resultSet == null)
			return null;
		TopCustomer customer = new TopCustomer();
		customer.setId(resultSet.getInt("id"));
		customer.setFirstname(resultSet.getString("first_name"));
		customer.setLastname(resultSet.getString("last_name"));
		customer.setTotalCash(resultSet.getDouble("total_cash"));
		return customer;
	}

}
