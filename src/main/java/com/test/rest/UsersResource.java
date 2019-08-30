package com.test.rest;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersResource {
	DBApi dbApi = new DBApi();
//	static {
//		User u1 = new User();
//        User u2 = new User();
//        u1.addPhoneNumber("348953948934");
//        u1.addPhoneNumber("5648496468468");
//        u1.addPhoneNumber("5648496468469");
//        u1.addPhoneNumber("5648496468461");
//        u1.addPhoneNumber("5648496468462");
//        u1.setAddress(new Address("Bla bla", "Pleven"));
//        DBSerializer.persistUser(u1);
//        DBSerializer.persistUser(u2);
//	}
	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		try {
			return Response.ok().entity(dbApi.loadAllUsers()).build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("id") String id) {
		try {
			User user = dbApi.loadUser(id);
			if (user != null) {
				return Response.ok().entity(user).build();
			}
			return Response.status(204).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {
		try {
			return Response.status(201).entity(dbApi.createUser(user)).build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(User user, @PathParam("id") String id) {
		try {
			user.setId(id);
			dbApi.putUser(user);
		} catch (SQLException e) {
			return Response.status(500).build();
		} catch (WebApplicationException e) {
			return e.getResponse();
		}
		return Response.ok().entity(user).build();
	}

	@DELETE
	@Path("/all")
	public Response deleteAllUsers() {
		try {
			dbApi.deleteAllUsers();
			return Response.ok().entity("Users deleted").build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") String id) {
		try {
			dbApi.deleteUser(id);
			return Response.ok().entity("User deleted if existed").build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}
}
