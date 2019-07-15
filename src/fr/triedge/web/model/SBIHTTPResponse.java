package fr.triedge.web.model;

import java.util.ArrayList;

public class SBIHTTPResponse {

	private ArrayList<String> headers = new ArrayList<>();
	private byte[] body;
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public ArrayList<String> getHeaders() {
		return headers;
	}
	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}
}
