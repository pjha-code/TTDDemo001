package com.ttd.demo001.service;

import java.util.Map;

public interface InvoiceService {
	public String updateInvoice(Map<Integer, String> records);

	public String getAllInvoices();

	public String getSingleInvoice(String invoiceNumber);

	public String validateInvoice(String requestObject);
}
