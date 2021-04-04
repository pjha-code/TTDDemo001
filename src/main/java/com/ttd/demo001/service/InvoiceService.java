package com.ttd.demo001.service;

import java.net.HttpURLConnection;

public interface InvoiceService {
	abstract public String getResponsePayload(HttpURLConnection conn);

	abstract public String setRequestPayloadForResponse(String requestPayload, HttpURLConnection conn);
}
