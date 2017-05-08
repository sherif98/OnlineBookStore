package application.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Repo {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "";
	private static final Repo instance = new Repo();

	private Connection con;

	private Repo() {
		try {
			Class.forName(JDBC_DRIVER);
			this.con = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static Repo getInstance() {
		return Repo.instance;
	}

	public ResultSet execute(String query) {
		ResultSet res = null;
		try {
			Statement statement = this.con.createStatement();
			res = statement.executeQuery(query);
		} catch (Exception e) {
			System.out.println("error while executing query: " + e);
		}
		return res;
	}

	public PreparedStatement getPreparedStatement(String template) {
		PreparedStatement res = null;
		try {
			res = this.con.prepareStatement(template);
		} catch (Exception e) {
			System.out.println("Exception during creating prepared statement: " + e);
		}
		return res;
	}

	public CallableStatement getCallableStatement(String template) {
		CallableStatement res = null;
		try {
			res = this.con.prepareCall(template);
		} catch (Exception e) {
			System.out.println("Exception during creating callable statement: " + e);
		}
		return res;
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.con.setAutoCommit(autoCommit);
	}

	public void commit() throws SQLException {
		this.con.commit();
	}

	public void rollback() throws SQLException {
		this.con.rollback();
	}
}
