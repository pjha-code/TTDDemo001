package com.ttd.demo001.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttd.demo001.entity.CTRCloudRequestLog;
import com.ttd.demo001.repository.CTRCloudRequestLogRepository;

@Service
public class InvoiceResponseGeneratorServiceImpl implements InvoiceResponseGeneratorService {

	@Autowired
	private CTRCloudRequestLogRepository cloudRequestLoggerRepo;

	@Override
	public String getResponsePayload(HttpURLConnection conn, CTRCloudRequestLog cloudRequestLog) {
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
			StringBuffer sb = new StringBuffer();
			while (buffReader.ready()) {
				sb.append(buffReader.readLine());
			}
			responseStr = sb.toString();
//			System.out.println(responseStr);
			cloudRequestLog.setResponseCode((short) conn.getResponseCode());
			cloudRequestLog.setResponsePayload(responseStr);
			cloudRequestLog.setLastUpdateDate(new Date());
			cloudRequestLoggerRepo.save(cloudRequestLog);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseStr;
	}

	@Override
	public String setRequestPayloadForResponse(String requestPayload, HttpURLConnection conn,
			CTRCloudRequestLog cloudRequestLog) {
		try {
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(requestPayload);
			osw.flush();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getResponsePayload(conn, cloudRequestLog);
	}

}
