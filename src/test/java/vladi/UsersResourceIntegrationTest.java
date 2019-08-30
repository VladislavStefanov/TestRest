package vladi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.plexus.util.IOUtil;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.rest.DBApi;
import com.test.rest.User;
import com.test.rest.UsersResource;

public class UsersResourceIntegrationTest extends JerseyTest {
	ArrayList<User> expectedUsers = new ArrayList<User>(Arrays.asList(TestUsersUtil.generateTestUser().build(),
			TestUsersUtil.generateTestUser().build(), TestUsersUtil.generateTestUser().build()));

	@Override
	protected Application configure() {
		return new ResourceConfig(UsersResource.class);
	}

	@Override
	protected void configureClient(final ClientConfig config) {
		config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		config.property(TestProperties.LOG_TRAFFIC, true);
		config.property(TestProperties.DUMP_ENTITY, true);
		config.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
		config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING");
	}

	@Before
	public void createTests() {
		DBApi dbApi = new DBApi();
		try {
			for (User user : expectedUsers) {
				user = dbApi.createUser(user);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@After
	public void deleteTests() {
		DBApi dbApi = new DBApi();
		try {
			dbApi.deleteAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetOneUser() {
		User expectedUser = expectedUsers.get(0);

		User user = target("users/" + expectedUser.getId()).request().get(User.class);

		assertEquals(expectedUser, user);
	}

	@Test
	public void testGetAllUsers() {
		Response response = target("users").request().get();
		ObjectMapper userMapper = new ObjectMapper();
		ArrayList<User> users = null;
		try (InputStream is = (InputStream) response.getEntity();) {
			String userJson = IOUtil.toString(is);
			users = userMapper.readValue(userJson, new TypeReference<ArrayList<User>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(expectedUsers.size() == users.size() && expectedUsers.containsAll(users));
	}

	@Test
	public void testPostUser() {
		User expectedUser = expectedUsers.get(0);

		Response response = target("/users").request()
				.post(Entity.entity(expectedUser, MediaType.APPLICATION_JSON_TYPE));

		ObjectMapper userMapper = new ObjectMapper();
		User user = null;
		String userJson = null;
		try (InputStream is = (InputStream) response.getEntity();) {
			userJson = IOUtil.toString(is);
			user = userMapper.readValue(userJson, User.class);
		} catch (JsonParseException e) {

			e.printStackTrace();
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		expectedUser.setId(user.getId());
		assertEquals(expectedUser, user);

		expectedUser = TestUsersUtil.generateTestUser().setName(null).build();
		response = target("/users").request().post(Entity.entity(expectedUser, MediaType.APPLICATION_JSON_TYPE));

		userMapper = new ObjectMapper();
		user = null;
		userJson = null;
		try (InputStream is = (InputStream) response.getEntity();) {
			userJson = IOUtil.toString(is);
			user = userMapper.readValue(userJson, User.class);
		} catch (JsonParseException e) {

			e.printStackTrace();
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		expectedUser.setId(user.getId());
		assertEquals(expectedUser, user);
	}

	@Test
	public void testPutUser() {
		User expectedUser = expectedUsers.get(0);

		expectedUser.setName("por");

		Response response = target("/users/" + expectedUser.getId()).request()
				.put(Entity.entity(expectedUser, MediaType.APPLICATION_JSON));

		ObjectMapper userMapper = new ObjectMapper();
		User user = null;
		String userJson = null;
		try (InputStream is = (InputStream) response.getEntity();) {
			userJson = IOUtil.toString(is);
			user = userMapper.readValue(userJson, User.class);
		} catch (JsonParseException e) {

			e.printStackTrace();
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		assertEquals(expectedUser, user);

		response = target("/users/" + expectedUser.getId()).request()
				.put(Entity.entity(expectedUser, MediaType.APPLICATION_JSON));
		try (InputStream is = (InputStream) response.getEntity();) {
			userJson = IOUtil.toString(is);
			user = userMapper.readValue(userJson, User.class);
		} catch (JsonParseException e) {

			e.printStackTrace();
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		assertEquals(expectedUser, user);
	}

	@Test
	public void testDeleteUser() {
		User expectedUser = expectedUsers.get(0);

		Response response = target("/users/" + expectedUser.getId()).request().delete();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		User user = null;
		DBApi dbApi = new DBApi();
		try {
			user = dbApi.loadUser(expectedUser.getId());
		} catch (SQLException e) {

			e.printStackTrace();
		}

		assertNull(user);

		response = target("/users/" + expectedUser.getId()).request().delete();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		user = null;
		try {
			user = dbApi.loadUser(expectedUser.getId());
		} catch (SQLException e) {

			e.printStackTrace();
		}

		assertNull(user);
	}

	@Test
	public void testDeleteAllUsers() {

		Response response = target("/users/all").request().delete();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		ArrayList<User> users = null;
		DBApi dbApi = new DBApi();
		try {
			users = dbApi.loadAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertEquals(new ArrayList<User>(), users);

		response = target("/users/all").request().delete();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		users = null;
		try {
			users = dbApi.loadAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertEquals(new ArrayList<User>(), users);
	}
}
