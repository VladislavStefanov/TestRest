package com.test.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;



public class UsersPreparedStatementMaker {
	
	private final static String USER_ID_QUERY = "select id from users where id = ?";
	private final static String USER_QUERY = "select users.name, users.password, addresses.street, addresses.city from users inner join addresses on users.id = addresses.userId and users.id = ?";
	private final static String ALL_USERS_QUERY = "select users.id, users.name, users.password, addresses.street, addresses.city from users inner join addresses on users.id = addresses.userId";
	private final static String PHONE_NUMBERS_QUERY = "select phoneNumber from phoneNumbers where userId = ?";

	private final static String INSERT_ADDRESS = "insert into addresses (id, street, city, userId) values (?, ?, ?, ?)";
	private final static String INSERT_USER = "insert into users (id, name, password) values (?, ?, ?)";
	private final static String INSERT_PHONE_NUMBERS = "insert into phoneNumbers (id, phoneNumber, userId) values (?, ?, ?)";

	private final static String UPDATE_USER = "update users set name = ?, password = ? where id = ?";
	private final static String UPDATE_ADDRESS = "update addresses set street = ?, city = ? where userId = ?";

	private final static String DELETE_PHONE_NUMBERS = "delete from phoneNumbers where userId = ?";
	private final static String DELETE_USER = "delete from users where id = ?";
	private final static String DELETE_ADDRESS = "delete from addresses where userId = ?";
	private final static String DELETE_ALL_USERS = "delete from users";
	private final static String DELETE_ALL_ADDRESSES = "delete from addresses";
	private final static String DELETE_ALL_PHONE_NUMBERS = "delete from phoneNumbers";
	
	public static PreparedStatement createUserIdQueryStatement (Connection connection, String id) throws SQLException {
		PreparedStatement userIdQueryStatement = connection.prepareStatement(USER_ID_QUERY);
		userIdQueryStatement.setString(1, id);
		return userIdQueryStatement;
	}
	
	public static PreparedStatement createUserQueryStatement(Connection connection, String id) throws SQLException {
		PreparedStatement userQueryStatement = connection.prepareStatement(USER_QUERY);
		userQueryStatement.setString(1, id);
		return userQueryStatement;
	}
	
	public static PreparedStatement createAllUsersQueryStatement(Connection connection) throws SQLException {
		PreparedStatement allUsersQueryStatement = connection.prepareStatement(ALL_USERS_QUERY);
		return allUsersQueryStatement;
	}
	
	public static PreparedStatement createPhoneNumbersQueryStatement(Connection connection, String id) throws SQLException {
		PreparedStatement userQueryStatement = connection.prepareStatement(PHONE_NUMBERS_QUERY);
		userQueryStatement.setString(1, id);
		return userQueryStatement;
	}
	
	public static PreparedStatement createInsertAddressStatement(Connection connection, User user, String addressId) throws SQLException {
		PreparedStatement insertAddressStatement = connection.prepareStatement(INSERT_ADDRESS);
		insertAddressStatement.setString(1, addressId);
		insertAddressStatement.setString(2, user.getAddress().getStreet());
		insertAddressStatement.setString(3, user.getAddress().getCity());
		insertAddressStatement.setString(4, user.getId());
		return insertAddressStatement;
		
	}
	
	public static PreparedStatement createInsertUserStatement(Connection connection, User user) throws SQLException {
		PreparedStatement insertUserStatement = connection.prepareStatement(INSERT_USER);
		insertUserStatement.setString(1, user.getId());
		insertUserStatement.setString(2, user.getName());
		insertUserStatement.setString(3, user.getPassword());
		return insertUserStatement;
	}
	
	public static PreparedStatement createInsertPhoneNumbersStatement(Connection connection, User user) throws SQLException {
		PreparedStatement insertPhoneNumbersStatement = connection.prepareStatement(INSERT_PHONE_NUMBERS);
		for (int i = 0; i < user.getPhoneNumbers().size(); i++) {
			insertPhoneNumbersStatement.setString(1, UUID.randomUUID().toString());
			insertPhoneNumbersStatement.setString(2, user.getPhoneNumber(i));
			insertPhoneNumbersStatement.setString(3, user.getId());
			insertPhoneNumbersStatement.addBatch();
			insertPhoneNumbersStatement.clearParameters();
		}
		return insertPhoneNumbersStatement;
	}
	
	public static PreparedStatement createUpdateUserStatement(Connection connection, User user) throws SQLException {
		PreparedStatement updateUserStatement = connection.prepareStatement(UPDATE_USER);
		updateUserStatement.setString(1, user.getName());
		updateUserStatement.setString(2, user.getPassword());
		updateUserStatement.setString(3, user.getId());
		return updateUserStatement;
	}
	
	public static PreparedStatement createUpdateAddressStatement(Connection connection, User user) throws SQLException {
		PreparedStatement updateAddressStatement = connection.prepareStatement(UPDATE_ADDRESS);
		updateAddressStatement.setString(1, user.getAddress().getStreet());
		updateAddressStatement.setString(2, user.getAddress().getCity());
		updateAddressStatement.setString(3, user.getId());
		return updateAddressStatement;
	}
	
	public static PreparedStatement createDeletePhoneNumbersStatement(Connection connection, String id) throws SQLException {
		PreparedStatement deletePhoneNumbersStatement = connection.prepareStatement(DELETE_PHONE_NUMBERS);
		deletePhoneNumbersStatement.setString(1, id);
		return deletePhoneNumbersStatement;
	}
	
	public static PreparedStatement createDeleteUserStatement(Connection connection, String id) throws SQLException {
		PreparedStatement deleteUserStatement = connection.prepareStatement(DELETE_USER);
		deleteUserStatement.setString(1, id);
		return deleteUserStatement;
	}
	
	public static PreparedStatement createDeleteAddressStatement(Connection connection, String id) throws SQLException {
		PreparedStatement deleteAddressStatement = connection.prepareStatement(DELETE_ADDRESS);
		deleteAddressStatement.setString(1, id);
		return deleteAddressStatement;
	}
	
	public static PreparedStatement createDeleteAllUsersStatement(Connection connection) throws SQLException {
		PreparedStatement deleteAllUsersStatement = connection.prepareStatement(DELETE_ALL_USERS);
		return deleteAllUsersStatement;
	}
	
	public static PreparedStatement createDeleteAllAddressesStatement(Connection connection) throws SQLException {
		PreparedStatement deleteAllAddressesStatement = connection.prepareStatement(DELETE_ALL_ADDRESSES);
		return deleteAllAddressesStatement;
	}
	
	public static PreparedStatement createDeleteAllPhoneNumbersStatement(Connection connection) throws SQLException {
		PreparedStatement deleteAllPhoneNumbersStatement = connection.prepareStatement(DELETE_ALL_PHONE_NUMBERS);
		return deleteAllPhoneNumbersStatement;
	}
	
	
	
}
