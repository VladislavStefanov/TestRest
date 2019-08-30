package com.test.rest;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

public class DBApi {

	public DBApi() {
	}

	private Connection getConnection() throws SQLException {
		DriverManager.registerDriver(driver);
		connection.setAutoCommit(false);
		return connection;
	}

	public void putUser(User user) throws SQLException, WebApplicationException {
		try {
			if (checkIfUserExists(user.getId())) {
				replaceUser(user);
				return;
			}
			throw new WebApplicationException(404);
		} catch (SQLException e) {
			throw e;
		}
	}

	public User loadUser(String id) throws SQLException {

		try (Connection connection = getConnection();
				PreparedStatement userQueryStatement = UsersPreparedStatementMaker.createUserQueryStatement(connection,
						id);
				ResultSet userResultSet = userQueryStatement.executeQuery()) {
			if (!userResultSet.next()) {
				return null;
			}
			String name = userResultSet.getString("name");
			String password = userResultSet.getString("password");
			String street = userResultSet.getString("street");
			String city = userResultSet.getString("city");
			ArrayList<String> phoneNumbers = loadPhoneNumbers(id);

			return new User.UserBuilder().setId(id).setName(name).setPassword(password)
					.setAddress(new Address(street, city)).setPhoneNumbers(phoneNumbers).build();
		}
	}

	public ArrayList<User> loadAllUsers() throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement allUsersQueryStatement = UsersPreparedStatementMaker
						.createAllUsersQueryStatement(connection);
				ResultSet allUsersResultSet = allUsersQueryStatement.executeQuery();) {
			ArrayList<User> users = new ArrayList<User>();
			while (allUsersResultSet.next()) {
				String id = allUsersResultSet.getString("id");
				String name = allUsersResultSet.getString("name");
				String password = allUsersResultSet.getString("password");
				String street = allUsersResultSet.getString("street");
				String city = allUsersResultSet.getString("city");
				ArrayList<String> phoneNumbers = loadPhoneNumbers(id);

				users.add(new User.UserBuilder().setId(id).setName(name).setPassword(password)
						.setAddress(new Address(street, city)).setPhoneNumbers(phoneNumbers).build());
			}
			return users;
		}
	}

	public void deleteUser(String id) throws SQLException {

		try (Connection connection = getConnection()) {
			try (PreparedStatement deleteUserStatement = UsersPreparedStatementMaker
					.createDeleteUserStatement(connection, id);
					PreparedStatement deleteAddressStatement = UsersPreparedStatementMaker
							.createDeleteAddressStatement(connection, id);) {
				deletePhoneNumbers(connection, id);
				deleteAddressStatement.executeUpdate();
				deleteUserStatement.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw e;
			}
		}
	}

	public void deleteAllUsers() throws SQLException {

		try (Connection connection = getConnection();) {
			try (PreparedStatement deleteAllPhoneNumbersStatement = UsersPreparedStatementMaker
					.createDeleteAllPhoneNumbersStatement(connection);
					PreparedStatement deleteAllUsersStatement = UsersPreparedStatementMaker
							.createDeleteAllUsersStatement(connection);
					PreparedStatement deleteAllAddressesStatement = UsersPreparedStatementMaker
							.createDeleteAllAddressesStatement(connection);) {
				deleteAllPhoneNumbersStatement.executeUpdate();
				deleteAllAddressesStatement.executeUpdate();
				deleteAllUsersStatement.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				throw e;
			}
		}
	}

	public User createUser(User user) throws SQLException {
		String id = UUID.randomUUID().toString();
		user.setId(id);
		try (Connection connection = getConnection()) {
			try {
				insertUser(connection, user);
				insertAddress(connection, user);
				insertPhoneNumbers(connection, user);
				
				connection.commit();
				
				return user;
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				throw e;
			}
		}
	}

	private void replaceUser(User user) throws SQLException {

		try (Connection connection = getConnection()) {
			try {
				updateUser(connection, user);
				updateAddress(connection, user);
				deletePhoneNumbers(connection, user.getId());
				insertPhoneNumbers(connection, user);
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				throw e;
			}
		}
	}

	private ArrayList<String> loadPhoneNumbers(String id) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement phoneNumbersQueryStatement = UsersPreparedStatementMaker
						.createPhoneNumbersQueryStatement(connection, id);
				ResultSet phoneNumbersResultSet = phoneNumbersQueryStatement.executeQuery();) {
			ArrayList<String> phoneNumbers = new ArrayList<String>();

			while (phoneNumbersResultSet.next()) {
				phoneNumbers.add(phoneNumbersResultSet.getString("phoneNumber"));
			}
			return phoneNumbers;
		}
	}

	private boolean checkIfUserExists(String id) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement userIdQueryStatement = UsersPreparedStatementMaker
						.createUserIdQueryStatement(connection, id);
				ResultSet userExistResultSet = userIdQueryStatement.executeQuery();) {
			return userExistResultSet.next();
		}
	}

	private void insertAddress(Connection connection, User user) throws SQLException {
		String addressId = UUID.randomUUID().toString();
		try (PreparedStatement insertAddressStatement = UsersPreparedStatementMaker
				.createInsertAddressStatement(connection, user, addressId)) {
			insertAddressStatement.executeUpdate();
		}
	}

	private void insertUser(Connection connection, User user) throws SQLException {
		try (PreparedStatement insertUserStatement = UsersPreparedStatementMaker.createInsertUserStatement(connection,
				user)) {
			insertUserStatement.executeUpdate();
		}
	}

	private void insertPhoneNumbers(Connection connection, User user) throws SQLException {
		try (PreparedStatement insertPhoneNumbersStatement = UsersPreparedStatementMaker
				.createInsertPhoneNumbersStatement(connection, user)) {
			insertPhoneNumbersStatement.executeBatch();
		}
	}

	private void updateUser(Connection connection, User user) throws SQLException {
		try (PreparedStatement updateUserStatement = UsersPreparedStatementMaker.createUpdateUserStatement(connection,
				user)) {
			updateUserStatement.executeUpdate();
		}
	}

	private void updateAddress(Connection connection, User user) throws SQLException {
		try (PreparedStatement updateAddressStatement = UsersPreparedStatementMaker
				.createUpdateAddressStatement(connection, user)) {
			updateAddressStatement.executeUpdate();
		}
	}

	private void deletePhoneNumbers(Connection connection, String id) throws SQLException {
		try (PreparedStatement deleteUserPhoneNumbers = UsersPreparedStatementMaker
				.createDeletePhoneNumbersStatement(connection, id);) {
			deleteUserPhoneNumbers.executeUpdate();
		}
	}
}
