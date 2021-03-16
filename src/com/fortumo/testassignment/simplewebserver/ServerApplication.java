/**
 * 
 */
package com.fortumo.testassignment.simplewebserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * test assignment for Junior Software Engineer position
 * @author Leo Tamm
 *
 */

public class ServerApplication {

	public static final int serverPort = 1337;
	public static final ThreadPoolExecutor threadExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(20);


	public static void main(String[] args) throws Exception {

		// create server at local machine with provided port
		// we'll be queuing 30 incoming TCP connections not to loose any data, the number should be adjusted for optimal performance
		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", serverPort), 30);

		// assign handler to root
		server.createContext("/", new CounterHttpHandler());

		// create thread manager
		server.setExecutor(threadExecutor);

		server.start();

		System.out.println("Server started at port " + serverPort + " ... ready for receiving data" + "\n");
		
	}	

}
