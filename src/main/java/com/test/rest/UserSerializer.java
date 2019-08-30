package com.test.rest;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserSerializer {
	public static String UserToJson(User user) throws JsonProcessingException
	{
		ObjectMapper userMapper = new ObjectMapper();
		return userMapper.writeValueAsString(user);

	}
	
	public static String UsersToJson(ArrayList<User> users) throws JsonProcessingException
	{
		ObjectMapper userMapper = new ObjectMapper();
		return userMapper.writeValueAsString(users);
	} 
	
	public static User JsonToUser(String userJson) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper userMapper = new ObjectMapper();
		return userMapper.readValue(userJson, User.class);
	}
}
