package fr.triedge.web.model;

import java.util.ArrayList;
import java.util.Date;

public class ResponseFactory {

	public static final SBIHTTPResponse create404Response(byte[] data) {
		SBIHTTPResponse response = new SBIHTTPResponse();
		ArrayList<String> headers = createDefaultHeaders();
		headers.add(0,"HTTP/1.1 404 Not Found");
		headers.add("Content-length: "+data.length);
		headers.add("");
		response.setHeaders(headers);
		response.setBody(data);
		return response;
	}
	
	public static final SBIHTTPResponse createDefaultResponse(byte[] data) {
		SBIHTTPResponse response = new SBIHTTPResponse();
		ArrayList<String> headers = createDefaultHeaders();
		headers.add(0,"HTTP/1.1 200 Not Found");
		headers.add("Content-length: "+data.length);
		headers.add("");
		response.setHeaders(headers);
		response.setBody(data);
		return response;
	}
	
	public static final ArrayList<String> createDefaultHeaders() {
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Server: SBIWebServer HTTP");
		headers.add("Date: "+ new Date());
		headers.add("Content-type: text/html");
		return headers;
	}
}
