package com.ttd.demo001.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Override
	public String getResponsePayload(HttpURLConnection conn) {
		String responseStr = null;
		try {
			Integer responseCode = conn.getResponseCode() / 100;
			InputStream in;
			if (responseCode == 2 || responseCode == 3)
				in = conn.getInputStream();
			else
				in = conn.getErrorStream();

			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader buffReader = new BufferedReader(reader);
			StringBuilder sb = new StringBuilder();
			while (buffReader.ready()) {
				sb.append(buffReader.readLine());
			}
			responseStr = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseStr;
	}

	@Override
	public String setRequestPayloadForResponse(String requestPayload, HttpURLConnection conn) {
		try {
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/vnd.oracle.adf.action+json");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(requestPayload);
			osw.flush();
			osw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getResponsePayload(conn);
	}

}
