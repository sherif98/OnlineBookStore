package application.db.repositories;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Cart;
import application.db.entities.Sold;

public class CartRepo {
	private Repo repo = Repo.getInstance();
	private static CartRepo instance = new CartRepo();
	private static final String SELECT_BY_CUSTOMER_ID_TEMPLATE = "select * from cart where customer_id = ?";
	private static final String SELECT_BY_CUSTOMER_ID_PAGING_TEMPLATE = "select * from cart where customer_id = ? limit ? offset ?";
	// private static final String CHECK_OUT_CART_TEMPLATE = "{call
	// check_out_cart(?)}";
	private static final String DELETE_CART_TEMPLATE = "delete from cart where cart.id = ?";
	private static final String BUY_BOOK_TEMPLATE = "{call buy_book(?, ?, ?)}";
	private static final String UPDATE_TEMPATE = "update cart set customer_id = ?, ISBN = ?, quantity = ?, selling_price = ? where id = ?";
	private static final String SELECT_BY_ID_TEMPLATE = "select * from cart where id = ?";
	private static final String SELECT_ALL_TEMPLATE = "select * from cart";
	private static final String SELECT_WITH_PAGING = "select * from cart limit ? offset ?";

	private PreparedStatement selectByCustomerId;
	private PreparedStatement selectByCustomerIdPaging;
	// private PreparedStatement checkOutCart;
	private PreparedStatement deleteCart;
	private PreparedStatement buyBook;
	private PreparedStatement update;
	private PreparedStatement selectById;
	private PreparedStatement selectAll;
	private PreparedStatement selectWithPaging;

	private CartRepo() {
		this.selectByCustomerId = repo.getPreparedStatement(SELECT_BY_CUSTOMER_ID_TEMPLATE);
		this.selectByCustomerIdPaging = repo.getPreparedStatement(SELECT_BY_CUSTOMER_ID_PAGING_TEMPLATE);
		// this.checkOutCart =
		// repo.getCallableStatement(CHECK_OUT_CART_TEMPLATE);
		this.deleteCart = repo.getPreparedStatement(DELETE_CART_TEMPLATE);
		this.buyBook = repo.getCallableStatement(BUY_BOOK_TEMPLATE);
		this.update = repo.getPreparedStatement(UPDATE_TEMPATE);
		this.selectById = repo.getPreparedStatement(SELECT_BY_ID_TEMPLATE);
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.selectWithPaging = repo.getPreparedStatement(SELECT_WITH_PAGING);
	}

	public List<Cart> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<Cart> cartList = new ArrayList<>();
		while (resultSet.next()) {
			cartList.add(this.buildCart(resultSet));
		}
		return cartList;
	}

	public List<Cart> selectWithPaging(int beginIndex, int pageSize) throws SQLException {
		selectWithPaging.setInt(1, pageSize);
		selectWithPaging.setInt(2, beginIndex);
		ResultSet resultSet = selectWithPaging.executeQuery();
		List<Cart> cartList = new ArrayList<>();
		while (resultSet.next()) {
			cartList.add(this.buildCart(resultSet));
		}
		return cartList;
	}

	public void update(Cart cart) throws SQLException {
		this.update.setInt(1, cart.getCustomerId());
		this.update.setInt(2, cart.getISBN());
		this.update.setInt(3, cart.getQuantity());
		this.update.setDouble(4, cart.getSellingPrice());
		this.update.setInt(5, cart.getId());
		this.update.executeUpdate();
	}

	public static CartRepo getInstance() {
		return instance;
	}

	public List<Cart> selectById(int id) throws SQLException {
		this.selectById.setInt(1, id);
		ResultSet resultSet = this.selectById.executeQuery();
		List<Cart> cartList = new ArrayList<>();
		while (resultSet.next()) {
			cartList.add(this.buildCart(resultSet));
		}
		return cartList;
	}

	public List<Cart> selectByCustomerId(int customerId) throws SQLException {
		this.selectByCustomerId.setInt(1, customerId);
		ResultSet resultSet = this.selectByCustomerId.executeQuery();
		List<Cart> cartList = new ArrayList<>();
		while (resultSet.next()) {
			cartList.add(this.buildCart(resultSet));
		}
		return cartList;
	}

	public List<Cart> selectByCustomerIdPaging(int customerId, int pageNumber, int pageSize) throws SQLException {
		int beginIndex = pageNumber * pageSize;
		this.selectByCustomerIdPaging.setInt(1, customerId);
		this.selectByCustomerIdPaging.setInt(2, pageSize);
		this.selectByCustomerIdPaging.setInt(3, beginIndex);
		ResultSet resultSet = this.selectByCustomerIdPaging.executeQuery();
		List<Cart> cartList = new ArrayList<>();
		while (resultSet.next()) {
			cartList.add(this.buildCart(resultSet));
		}
		return cartList;
	}

	private Cart buildCart(ResultSet resultSet) throws SQLException {
		if (resultSet == null) {
			return null;
		}
		Cart cart = new Cart();
		cart.setId(resultSet.getInt("id"));
		cart.setCustomerId(resultSet.getInt("customer_id"));
		cart.setISBN(resultSet.getInt("ISBN"));
		cart.setQuantity(resultSet.getInt("quantity"));
		cart.setSellingPrice(resultSet.getDouble("selling_price"));
		return cart;
	}

	public void checkOut(int customerId) throws SQLException {
		repo.setAutoCommit(false);
		try {
			List<Cart> customerCarts = selectByCustomerId(customerId);
			for (Cart cart : customerCarts) {
				deleteCart(cart.getId());
			}
			SoldRepo soldRepo = SoldRepo.getInstance();
			for (Cart cart : customerCarts) {
				Sold sold = new Sold();
				sold.setCustomerId(customerId);
				sold.setISBN(cart.getISBN());
				sold.setQuantity(cart.getQuantity());
				sold.setSellingPrice(cart.getSellingPrice());
				LocalDate localDate = LocalDate.now();
				Date date = java.sql.Date.valueOf(localDate);
				sold.setSellingDate(date);
				soldRepo.persist(sold);
			}
			repo.commit();
		} catch (Exception e) {
			repo.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			repo.setAutoCommit(true);
		}
		// this.checkOutCart.setInt(1,customerId);
		// this.checkOutCart.executeUpdate();
	}

	public void persist(Cart cart) throws SQLException {
		this.buyBook(cart.getCustomerId(), cart.getISBN(), cart.getQuantity());
	}

	public void deleteCart(int cartId) throws SQLException {
		this.deleteCart.setInt(1, cartId);
		this.deleteCart.executeUpdate();
	}

	public void buyBook(int customerId, int ISBN, int quantity) throws SQLException {
		this.buyBook.setInt(1, customerId);
		this.buyBook.setInt(2, ISBN);
		this.buyBook.setInt(3, quantity);
		this.buyBook.executeQuery();
	}
}
