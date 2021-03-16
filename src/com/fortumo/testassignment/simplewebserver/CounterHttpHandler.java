package com.fortumo.testassignment.simplewebserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CounterHttpHandler implements HttpHandler {


	private static Long sum = 0L;
	private static Long addThis;
	private static String reply;


	//@Override
	public void handle(HttpExchange handleHttpArgument) throws IOException {

		// extract data from POST request body input stream
		try {
			BufferedReader httpDataBufferReader = new BufferedReader(new InputStreamReader(handleHttpArgument.getRequestBody(), "UTF-8"));

			String request;

			StringBuilder inputStringBuilder = new StringBuilder();
			String tempString;

			while ((tempString = httpDataBufferReader.readLine()) != null) {

				inputStringBuilder.append(tempString);
			}

			httpDataBufferReader.close();

			request = inputStringBuilder.toString();

			// we are proceeding with POST requests
			if ("POST".equals(handleHttpArgument.getRequestMethod())) {

				// we are proceeding with non-empty strings
				if (request.length() > 0) {

					if (isLong(request)) {

						addThis = Long.parseLong(request);

						System.out.print("Log: " + String.valueOf(addThis) + " + " + String.valueOf(sum));

						sum += addThis;

						System.out.println(" = " + String.valueOf(sum));
						
						reply = "Received!";
						respondToRequest(handleHttpArgument, reply);
						reply = null;

					} else if (request.substring(0, 3).equals("end") && request.substring(3, 4).equals(" ") && request.length() > 4 ) {

						String id = request.split(" ")[1];

						reply = String.valueOf(sum) + " " + id;

						System.out.println("Log: replying " + reply );
						respondToRequest(handleHttpArgument, reply);

						sum = 0L;
						addThis = 0L;
						reply = null;

						System.out.println("Log: clearing variables: sum = " + String.valueOf(sum) + ", addThis = " + String.valueOf(addThis) + 
								", reply = " + reply);

					} else {

						reply = "Cannot handle this!";
						respondToRequest(handleHttpArgument, reply);
						reply = null;
					}

				} else {

					reply = "Empty request!";
					respondToRequest(handleHttpArgument, reply);
					reply = null;
				}

			} else  { 

				reply = "Invalid request declined!";
				respondToRequest(handleHttpArgument, reply);
				reply = null;
			}  

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	
	public static boolean isLong(String stringNumber) {

		if (stringNumber == null) {

			return false;
		}

		try {

			@SuppressWarnings("unused")
			Long longNumber = Long.parseLong(stringNumber);

		} catch (NumberFormatException nfe) {

			return false;
		}

		return true;
	}


	private void respondToRequest(HttpExchange respondHttpArgument, String response) throws IOException {

		OutputStream outputStream = respondHttpArgument.getResponseBody();

		StringBuilder htmlBuilder = new StringBuilder();

		htmlBuilder.append(response);

		String htmlResponse = htmlBuilder.toString();

		respondHttpArgument.sendResponseHeaders(200, htmlResponse.length());
		outputStream.write(htmlResponse.getBytes());
		outputStream.flush();
		outputStream.close();
	}

}
