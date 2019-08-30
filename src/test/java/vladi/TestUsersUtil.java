package vladi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.test.rest.Address;
import com.test.rest.User;

public class TestUsersUtil {
	private static ArrayList<String> words = new ArrayList<String>(
			Arrays.asList("jojo", "gogo", "dodo", "toto", "bobo", "zozo", "popo", "momo", "yoyo", "koko", "roro"));

	public static User.UserBuilder generateTestUser() {
		String id = UUID.randomUUID().toString();
		String name = words.get((int) Math.floor(Math.random() * words.size()));
		String password = words.get((int) Math.floor(Math.random() * words.size()));
		int phoneNumbersCount = (int) Math.floor(Math.random() * 10);
		ArrayList<String> phoneNumbers = new ArrayList<String>();
		for (int i = 0; i < phoneNumbersCount; i++) {
			phoneNumbers.add(Long.toString((long)Math.floor(Math.random() * 100000000)));
		}

		String street = words.get((int) Math.floor(Math.random() * words.size()));
		String city = words.get((int) Math.floor(Math.random() * words.size()));

		return new User.UserBuilder().setId(id).setName(name).setPassword(password).setPhoneNumbers(phoneNumbers)
				.setAddress(new Address(street, city));

	}
}
