package com.iiht.evaluation.eloan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.iiht.evaluation.eloan.dto.UserInfo;

public class ConnectionDao {
	private static final long serialVersionUID = 1L;
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public ConnectionDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }
	

	/*======================= SQL queries used in DAO file ====================*/
	static final String FETCH_USER_CRED = "select user_name, user_password from eloandb.user;";
	static final String FETCH_USER_INFO = "select * from eloandb.userinfo where user_name='%s'";
	
	/*=========================================================================*/
	

	public  Connection connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
		return jdbcConnection;
	}

	public void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	// put the relevant DAO methods here..
	
	public HashMap<String, String> getUserCredData()
	{
		Statement stmt = null;
		HashMap<String, String> cred_map = new HashMap<String, String>();
		
		try
		{
			jdbcConnection = this.connect();
			stmt = jdbcConnection.createStatement();
			ResultSet rs = stmt.executeQuery(FETCH_USER_CRED);

			while(rs.next())
			{
				cred_map.put(rs.getString("user_name"), rs.getString("user_password"));
			}
			return cred_map;
		} 
		catch(Exception e)
		{
			System.out.println("Unable to fetch User credential data");
			System.out.println("Error : "+e.getMessage());
			return cred_map;
		}
		finally 
		{
			try 
			{
				stmt.close();
				this.disconnect();
			} 
			catch (SQLException e) 
			{
				System.out.println("Error : "+ e.getMessage());
			}
		}
	}
		
		
	public UserInfo getUserInfo(String userName) 
	{
			Connection conn = null;
			Statement stmt = null;
			UserInfo userInfo = null;

			try
			{
				jdbcConnection = this.connect();
				stmt = jdbcConnection.createStatement();
				ResultSet rs = stmt.executeQuery(String.format(FETCH_USER_INFO,userName));
				while(rs.next())
				{
					System.out.println(rs.getString("USER_FIRSTNAME")+"-"+rs.getString("USER_LASTNAME"));
					
					userInfo = new UserInfo(rs.getString("USER_NAME"), rs.getString("USER_FIRSTNAME"), 
							rs.getString("USER_LASTNAME"), rs.getString("DOB"), rs.getString("MOBILE"), rs.getString("EMAIL"), 
							rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), 
							rs.getString("ZIPCODE"), rs.getString("CUSTOMER_ADDRESS"));
					
				}
				return userInfo;
			} 
			catch(Exception e)
			{
				System.out.println("Unable to fetch User Info data");
				System.out.println("Error : "+e.getMessage());
				return userInfo;
			}
			finally 
			{
				try 
				{
					stmt.close();
					this.disconnect();
				} 
				catch (SQLException e) 
				{
					System.out.println("Error : "+ e.getMessage());
				}
			}
	}

	
}
