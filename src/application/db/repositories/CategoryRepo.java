package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Category;

public class CategoryRepo {
	private Repo repo = Repo.getInstance();
	private static CategoryRepo instance = new CategoryRepo();
	private static final String SELECT_ALL_TEMPLATE = "select * from category";
	private static final String INSERT_TEMPLATE = "insert into category values(?, ?)";
	private static final String UPDATE_TEMPLATE = "update category set category_type = ? where id = ?";
	private static final String SELECT_BY_ID_TEMPLATE = "select * from category where id = ?";
	private static final String SELECT_BY_TYPE_TEMPLATE = "select * from category where category_type = ?";
	
	private PreparedStatement selectAll;
	private PreparedStatement insert;
	private PreparedStatement update;
	private PreparedStatement selectById;
	private PreparedStatement selectByType;
	
	private CategoryRepo() {
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.insert = repo.getPreparedStatement(INSERT_TEMPLATE);
		this.update = repo.getPreparedStatement(UPDATE_TEMPLATE);
		this.selectById = repo.getPreparedStatement(SELECT_BY_ID_TEMPLATE);
		this.selectByType = repo.getPreparedStatement(SELECT_BY_TYPE_TEMPLATE);
	}
	
	public Category selectByCategoryType(String type) throws SQLException {
		this.selectByType.setString(1, type);
		ResultSet resultSet = this.selectByType.executeQuery();
		resultSet.next();
		return this.buildCategory(resultSet);
	}
	
	public Category selectById(int id) throws SQLException {
		this.selectById.setInt(1, id);
		ResultSet resultSet = this.selectById.executeQuery();
		resultSet.next();
		return this.buildCategory(resultSet);
	}
	
	public void update(Category category) throws SQLException {
		this.update.setString(1, category.getType());
		this.update.setInt(2, category.getId());
		this.update.executeUpdate();
	}
	
	public static CategoryRepo getInstance() {
		return instance;
	}
	public List<Category> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<Category> categoryList = new ArrayList<>();
		while(resultSet.next()) {
			categoryList.add(this.buildCategory(resultSet));
		}
		return categoryList;
	}
	
	private Category buildCategory(ResultSet resultSet) throws SQLException {
		if(resultSet == null) {
			return null;
		}
		Category category = new Category();
		category.setId(resultSet.getInt("id"));
		category.setType(resultSet.getString("category_type"));
		return category;
	}
	
	public void persist(Category category) throws SQLException {
		this.insert.setInt(1, category.getId());
		this.insert.setString(2, category.getType());
		this.insert.executeUpdate();
	}
	
}
