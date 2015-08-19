package org.shonkolon;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.shonkolon.exception.ResourceNotFoundException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Path("/sub/{message}")
	// @Produces(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIt(@Context HttpHeaders hh, @PathParam("message") String message) {
		MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
		Map<String, Cookie> pathParams = hh.getCookies();

		System.out.println(headerParams);
		String entity = "asdfasdfasdfasdf";
		// System.out.println("Authentication scheme:"+sc.getUserPrincipal());
		// if (sc.isUserInRole("PreferredCustomer")) {
		// System.out.println("PreferredCustomer");
		// } else {
		// System.out.println("Not PreferredCustomer");
		// }
		// throw new ResourceNotFoundException("Custom not found for "+message);
		Response r = Response.ok(entity).build();
		// r.ok(entity);
		// r.ok().build();
		return r;
	}
}
