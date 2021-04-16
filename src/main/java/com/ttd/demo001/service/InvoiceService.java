package com.ttd.demo001.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public interface InvoiceService {
	public JSONObject updateInvoice(Map<Integer, String> records);

	public List<Map<String, String>> getAllInvoices();

	public JSONObject getSingleInvoice(String invoiceNumber);

	public String validateInvoice(String requestObject);
}
