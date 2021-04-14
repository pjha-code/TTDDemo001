package com.ttd.demo001.service;

import java.net.HttpURLConnection;

import com.ttd.demo001.entity.CTRCloudRequestLog;

public interface InvoiceResponseGeneratorService {
	abstract public String getResponsePayload(HttpURLConnection conn, CTRCloudRequestLog cloudRequestLog);

	abstract public String setRequestPayloadForResponse(String requestPayload, HttpURLConnection conn,
			CTRCloudRequestLog cloudRequestLog);
}
