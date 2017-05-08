package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Customer;

public class CustomerRepo {
	private Repo repo = Repo.getInstance();
	private static final String INSERT_TEMPLATE = "insert into customer "
			+ "(email, first_name, last_name, shipping_address, phone, is_manager, username, password) "
			+ "values(?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_BY_NAME_TEMPLATE = "select * from customer where customer.username = ? and customer.password = ?";
	private static final String SELECT_BY_ID_TEMPLATE = "select * from customer where customer.id = ?";
	private static final String PROMOT_TEMPLATE = "{call promot_customer(?)}";
	private static final String UPDATE_TEMPLATE = "update customer set email = ?, first_name = ?, last_name = ?, shipping_address = ?, phone = ? ,is_manager = ?, username = ?, password = ? where id = ?";
	private static final String SELECT_ALL_TEMPLATE = "select * from customer";
	private static final String SELECT_WITH_PAGING = "select * from customer limit ? offset ?";

	private static CustomerRepo instance = new CustomerRepo();

	private PreparedStatement insertStatement;
	private PreparedStatement selectByNameStatement;
	private PreparedStatement selectByIdStatement;
	private PreparedStatement promotStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement selectAllStatement;
	private PreparedStatement selectWithPaging;

	private CustomerRepo() {
		this.insertStatement = repo.getPreparedStatement(INSERT_TEMPLATE);
		this.selectByNameStatement = repo.getPreparedStatement(SELECT_BY_NAME_TEMPLATE);
		this.selectByIdStatement = repo.getPreparedStatement(SELECT_BY_ID_TEMPLATE);
		this.promotStatement = repo.getCallableStatement(PROMOT_TEMPLATE);
		this.updateStatement = repo.getPreparedStatement(UPDATE_TEMPLATE);
		this.selectAllStatement = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.selectWithPaging = repo.getPreparedStatement(SELECT_WITH_PAGING);
	}

	public static CustomerRepo getInstance() {
		return instance;
	}

	public Customer selectById(int id) throws SQLException {
		this.selectByIdStatement.setInt(1, id);
		ResultSet resultSet = selectByIdStatement.executeQuery();
		resultSet.next();
		return this.buildCustomer(resultSet);
	}

	public Customer selectByUsernameAndPassword(String username, String password) throws SQLException {
		selectByNameStatement.setString(1, username);
		selectByNameStatement.setString(2, password);
		ResultSet resultSet = selectByNameStatement.executeQuery();
		resultSet.next();
		return this.buildCustomer(resultSet);
	}

	public List<Customer> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAllStatement.executeQuery();
		List<Customer> customerList = new ArrayList<>();
		while(resultSet.next()) {
			customerList.add(this.buildCustomer(resultSet));
		}
		return customerList;
	}
	
	public List<Customer> selectWithPaging(int pageIndex, int pageSize) throws SQLException {
		int beginIndex = pageIndex * pageSize;
		selectWithPaging.setInt(1, pageSize);
		selectWithPaging.setInt(2, beginIndex);
		ResultSet resultSet = this.selectWithPaging.executeQuery();
		List<Customer> customerList = new ArrayList<>();
		while (resultSet.next()) {
			customerList.add(this.buildCustomer(resultSet));
		}
		return customerList;
	}
	
	public void persist(Customer customer) throws SQLException {
		insertStatement.setString(1, customer.getEmail());
		insertStatement.setString(2, customer.getFirstname());
		insertStatement.setString(3, customer.getLastname());
		insertStatement.setString(4, customer.getShippingAddress());
		insertStatement.setString(5, customer.getPhone());
		insertStatement.setBoolean(6, customer.isManager());
		insertStatement.setString(7, customer.getUsername());
		insertStatement.setString(8, customer.getPassword());
		insertStatement.executeUpdate();
	}
	
	public void update(Customer customer) throws SQLException {
		this.updateStatement.setString(1, customer.getEmail());
		this.updateStatement.setString(2, customer.getFirstname());
		this.updateStatement.setString(3, customer.getLastname());
		this.updateStatement.setString(4, customer.getShippingAddress());
		this.updateStatement.setString(5, customer.getPhone());
		this.updateStatement.setBoolean(6, customer.isManager());
		this.updateStatement.setString(7, customer.getUsername());
		this.updateStatement.setString(8, customer.getPassword());
		this.updateStatement.setInt(9, customer.getId());
		this.updateStatement.executeUpdate();
	}

	public void promot(int customerId) throws SQLException {
		promotStatement.setInt(1, customerId);
		promotStatement.executeQuery();
	}

	private Customer buildCustomer(ResultSet resultSet) throws SQLException {
		if (resultSet == null)
			return null;
		Customer customer = new Customer();
		customer.setId(resultSet.getInt("id"));
		customer.setEmail(resultSet.getString("email"));
		customer.setFirstname(resultSet.getString("first_name"));
		customer.setLastname(resultSet.getString("last_name"));
		customer.setPhone(resultSet.getString("phone"));
		customer.setShippingAddress(resultSet.getString("shipping_address"));
		customer.setUsername(resultSet.getString("username"));
		customer.setManager(resultSet.getBoolean("is_manager"));
		customer.setPassword(resultSet.getString("password"));
		return customer;
	}
}
