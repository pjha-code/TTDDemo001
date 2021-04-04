package com.ttd.demo001.controller;

import java.net.HttpURLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttd.demo001.service.InvoiceService;
import com.ttd.demo001.util.HTTPMethods;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

	@Value("${resource_url}")
	private String resourceUrl;

	@Autowired
	private com.ttd.demo001.util.HttpURLConnection conn;

	@Autowired
	private InvoiceService invoiceService;

	@GetMapping("/getAll")
	public String getAll() {
		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_GET, resourceUrl);
		return invoiceService.getResponsePayload(conn1);
	}

	@GetMapping("/getInvoice/{invoiceId}")
	public String get(@PathVariable(required = true) String invoiceId) {
		String updatedResourceURL = resourceUrl + "/" + invoiceId;
		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_GET, updatedResourceURL);
		return invoiceService.getResponsePayload(conn1);
	}

	@PostMapping(path = "/validateInvoice", consumes = "application/json", produces = { "application/json" })
	public String validate(@RequestBody String payload) {

		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_POST, resourceUrl);
		return invoiceService.setRequestPayloadForResponse(payload, conn1);
	}

}
