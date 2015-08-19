package org.shonkolon;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.glassfish.grizzly.http.server.HttpServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.shonkolon.datamodel.User;

/**
 * Hello world!
 */
public class App {

	/*
	 * private static final URI BASE_URI =
	 * URI.create("http://localhost:8080/base/"); public static final String
	 * ROOT_PATH = "helloworld";
	 * 
	 * public static void main(String[] args) { try { System.out.println(
	 * "\"Hello World\" Jersey Example App");
	 * 
	 * final ResourceConfig resourceConfig = new
	 * ResourceConfig(MyResource.class); final HttpServer server =
	 * GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
	 * 
	 * System.out.println(String.format(
	 * "Application started.\nTry out %s%s\nHit enter to stop it...", BASE_URI,
	 * ROOT_PATH)); System.in.read(); server.stop(); } catch (IOException ex) {
	 * Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex); }
	 * 
	 * } /
	 */
	private static final int DEFAULT_PORT = 8080;

	private int serverPort;

	public App(int serverPort) throws Exception {
		this.serverPort = serverPort;

		Server server = configureServer();
		server.start();
		server.join();
	}

	private Server configureServer() {
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.packages(MyResource.class.getPackage().getName());
		resourceConfig.register(JacksonFeature.class);
		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		ServletHolder sh = new ServletHolder(servletContainer);
		Server server = new Server(serverPort);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(sh, "/*");
		server.setHandler(context);
		return server;
	}

	public static void main(String[] args) throws Exception {

		int serverPort = DEFAULT_PORT;

		if (args.length >= 1) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		// User u = new User();
		// u.id=99999;
		// u.lastName="alam";
		// u.userName="mrl";
		// u.firstName="minhaj";
		// u.test();
		User u2 = new User();
		u2.loadById(1);
		System.out.println("loaded object: " + u2.firstName + u2.lastName);
		u2.dump();
		u2.firstName = "dddddddddd";
		u2.update();
		// u2.delete();
		// new App(serverPort);
	}

	// */
}
