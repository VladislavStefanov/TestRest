package vladi;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.test.rest.DBApi;
import com.test.rest.User;
import com.test.rest.UsersResource;

@RunWith(Parameterized.class)
public class UsersResourceParametrizedTest extends JerseyTest {
	
	private User actualUser;
	private User expectedUser;
	
	public UsersResourceParametrizedTest(User actualUser, User expectedUser) {
		this.actualUser = actualUser;
		this.expectedUser = expectedUser;
	}
	
	@Parameters
	public static ArrayList<User[]> expectedUsers() {
		ArrayList<User[]> testUserPairs = new ArrayList<User[]>();
		DBApi dbApi = new DBApi();
		for (int i = 0; i < 10; i++) {
			User user = TestUsersUtil.generateTestUser().build();
			try {
				User expectedUser = dbApi.createUser(user);
				testUserPairs.add(new User[] { user, expectedUser });
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return testUserPairs;
	}

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

	@Test
	public void test() {
		actualUser = target("users/" + expectedUser.getId()).request().get(User.class);

		assertEquals(expectedUser, actualUser);
	}
	@AfterClass
	public static void deleteAllTestUsers() {
		DBApi dbApi = new DBApi();
		try {
			dbApi.deleteAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}