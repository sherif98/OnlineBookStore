package application.db.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.db.Repo;
import application.db.entities.Author;

public class AuthorRepo {
	private Repo repo = Repo.getInstance();
	private static AuthorRepo instance = new AuthorRepo();
	private static final String SELECT_ALL_TEMPLATE = "select * from author";
	private static final String INSERT_TEMPLATE = "insert into author values(?, ?)";
	private static final String SELECT_BOOK_AUTHORS_TEMPLATE = "select * from author where ISBN = ?";
	
	private PreparedStatement selectAll;
	private PreparedStatement insert;
	private PreparedStatement selectBookAuthors;
	
	private AuthorRepo() {
		this.selectAll = repo.getPreparedStatement(SELECT_ALL_TEMPLATE);
		this.insert = repo.getPreparedStatement(INSERT_TEMPLATE);
		this.selectBookAuthors = repo.getPreparedStatement(SELECT_BOOK_AUTHORS_TEMPLATE);
	}
	
	public static AuthorRepo getInstance() {
		return instance;
	}
	
	public List<Author> getBookAuthors(int ISBN) throws SQLException {
		this.selectBookAuthors.setInt(1, ISBN);
		ResultSet resultSet = this.selectBookAuthors.executeQuery();
		List<Author> authorList = new ArrayList<>();
		while(resultSet.next()) {
			authorList.add(this.buildAuthor(resultSet));
		}
		return authorList;
	}
	
	public List<Author> selectAll() throws SQLException {
		ResultSet resultSet = this.selectAll.executeQuery();
		List<Author> authorList = new ArrayList<>();
		while(resultSet.next()) {
			authorList.add(this.buildAuthor(resultSet));
		}
		return authorList;
	}
	
	private Author buildAuthor(ResultSet resultSet) throws SQLException {
		if(resultSet == null) {
			return null;
		}
		Author author = new Author();
		author.setName(resultSet.getString("author_name"));
		author.setISBN(resultSet.getInt("ISBN"));
		return author;
	}
	
	public void persist(Author author) throws SQLException {
		this.insert.setInt(1, author.getISBN());
		this.insert.setString(2, author.getName());
		this.insert.executeUpdate();
	}
	
}
