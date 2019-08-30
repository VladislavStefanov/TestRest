package com.test.rest;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
//@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class User {
	@Getter
	private String id = "no-id";
	@Getter
	private String name = "no-name";
	@Getter
	private String password = "no-pass";
	@Getter
	private ArrayList<String> phoneNumbers = new ArrayList<String>();
	private Address address = new Address();

	public User() {

	}

	public User(String name, String password, ArrayList<String> phoneNumbers, Address address) {
		this.name = name;
		this.password = password;
		this.phoneNumbers.addAll(phoneNumbers);
		this.address = address;
	}

	public User(String id, String name, String password, ArrayList<String> phoneNumbers, Address address) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.phoneNumbers.addAll(phoneNumbers);
		this.address = address;
	}

	public void addPhoneNumber(String phoneNumber) {
		this.phoneNumbers.add(phoneNumber);
	}

	public String getPhoneNumber(int index) {
		return this.phoneNumbers.get(index);
	}

	public void removePhoneNumber(int index) {
		this.phoneNumbers.remove(index);
	}

	public Address getAddress() {
		if (address == null) {
			return new Address("none", "none");
		}
		return address;
	}

	public void setId(String id) {
		if (id == null) {
			this.id = "no-id";
			return;
		}
		this.id = id;
	}

	public void setName(String name) {
		if (name == null) {
			this.name = "no-name";
			return;
		}
		this.name = name;
	}

	public void setPassword(String password) {
		if (password == null) {
			this.password = "no-password";
			return;
		}
		this.password = password;
	}

	public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
		if (phoneNumbers == null) {
			this.phoneNumbers = new ArrayList<String>();
			return;
		}
		this.phoneNumbers = phoneNumbers;
	}

	public void setAddress(Address address) {
		if (address == null) {
			this.address = new Address();
			return;
		}
		this.address = address;
	}

	public static class UserBuilder {
		private User user;

		public UserBuilder() {
			user = new User();
		}

		public UserBuilder(User user) {
			this.user = new User.UserBuilder().setId(user.getId()).setName(user.getName())
					.setPassword(user.getPassword()).setPhoneNumbers(new ArrayList<String>(user.getPhoneNumbers()))
					.setAddress(new Address(user.getAddress().getStreet(), user.getAddress().getCity())).build();
		}

		public UserBuilder setId(String id) {
			user.setId(id);
			return this;
		}

		public UserBuilder setName(String name) {
			user.setName(name);
			return this;
		}

		public UserBuilder setPassword(String password) {
			user.setPassword(password);
			return this;
		}

		public UserBuilder setPhoneNumbers(ArrayList<String> phoneNumbers) {
			user.getPhoneNumbers().clear();
			user.getPhoneNumbers().addAll(phoneNumbers);
			return this;
		}

		public UserBuilder addPhoneNumber(String phoneNumber) {
			user.addPhoneNumber(phoneNumber);
			return this;
		}

		public UserBuilder setAddress(Address address) {
			user.setAddress(address);
			return this;
		}

		public User build() {
			return user;
		}
	}

}
