package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Publisher;

public class PublisherRepo {
	
	private Repo repo = Repo.getInstance();
	private static PublisherRepo instance = new PublisherRepo();
	private static final String INSERT_TEMPLATE = "insert into publisher values (?, ?, ?)";
	private static final String SELECT_ALL_TEMPLATE = "select * from publisher";
	private static final String UPDATE_TEMPLATE = "update publisher set address = ?, phone = ? where publisher_name = ?";
	
	private PreparedStatement insert;
	private PreparedStatement selectAll;
	private PreparedStatement update;
	
	private PublisherRepo() {
		this.insert = repo.getPreparedStatement(INSERT_TEMPLATE);
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.update = repo.getPreparedStatement(UPDATE_TEMPLATE);
	}
	
	public void update(Publisher publisher) throws SQLException {
		this.update.setString(1, publisher.getAddress());
		this.update.setString(2, publisher.getPhone());
		this.update.setString(3, publisher.getPublisherName());
		this.update.executeUpdate();
	}
	
	public static PublisherRepo getInstance() {
		return instance;
	}
	
	public void persist(Publisher publisher) throws SQLException {
		this.insert.setString(1, publisher.getPublisherName());
		this.insert.setString(2, publisher.getAddress());
		this.insert.setString(3, publisher.getPhone());
		this.insert.executeUpdate();
	}
	
	public List<Publisher> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<Publisher> publisherList = new ArrayList<>();
		while(resultSet.next()) {
			publisherList.add(this.buildPublisher(resultSet));
		}
		return publisherList;
	}

	private Publisher buildPublisher(ResultSet resultSet) throws SQLException {
		if(resultSet == null) {
			return null;
		}
		Publisher publisher = new Publisher();
		publisher.setPublisherName(resultSet.getString("publisher_name"));
		publisher.setPhone(resultSet.getString("phone"));
		publisher.setAddress(resultSet.getString("address"));
		return publisher;
	}

}
