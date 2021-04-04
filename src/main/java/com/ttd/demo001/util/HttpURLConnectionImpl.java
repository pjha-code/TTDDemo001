package com.ttd.demo001.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpURLConnectionImpl implements HttpURLConnection {

	@Value("${u}")
	private String username;
	@Value("${p}")
	private String password;
	@Value("${cloud_url}")
	private String serverUrl;

	public java.net.HttpURLConnection getConnection(String httpMethod, String resourceUrl) {

		java.net.HttpURLConnection conn = null;
		try {
			URL url = new URL(serverUrl + resourceUrl);

			conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setRequestMethod(httpMethod);
			if (username != null && password != null) {
				getBasicAuthHeader(conn);
			} else {
				throw new IOException("username or password or both not found");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return conn;
	}

	private void getBasicAuthHeader(java.net.HttpURLConnection conn) {
		String creds = username + ":" + password;
		String base64Encoded = Base64.getEncoder().encodeToString(creds.getBytes());
		String auth = "Basic " + base64Encoded;
		conn.setRequestProperty("Authorization", auth);
	}

}
