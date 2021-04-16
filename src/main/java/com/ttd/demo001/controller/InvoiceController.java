package com.ttd.demo001.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ttd.demo001.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
@CrossOrigin
public class InvoiceController {

	@Value("${resource_url}")
	private String resourceUrl;
	@Autowired
	private InvoiceService invoiceService;

	@PostMapping("/updateInvoice")
	public JSONObject updateInvoice(@RequestParam("file") MultipartFile file) {

		JSONObject resp = null;
		String[] filenameParts = file.getOriginalFilename().split("\\.");
		String fileType = filenameParts[filenameParts.length - 1].toUpperCase();

		Map<Integer, String> records = new HashMap<>();

		if ("CSV".equals(fileType)) {
			try {
				InputStream stream = file.getInputStream();
				InputStreamReader reader = new InputStreamReader(stream);
				BufferedReader buffReader = new BufferedReader(reader);

				int keyCount = 0;
				while (buffReader.ready()) {
					String record = buffReader.readLine();

					records.put(keyCount++, record);
				}
				resp = invoiceService.updateInvoice(records);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}

	@GetMapping("/getAll")
	public List<Map<String, String>> getAll() {
		return invoiceService.getAllInvoices();
	}

	@GetMapping("/getInvoice/{invoiceNumber}")
	public JSONObject get(@PathVariable(required = true) String invoiceNumber) {
		return invoiceService.getSingleInvoice(invoiceNumber);
	}

	@PostMapping(path = "/validateInvoice", consumes = "application/json", produces = { "application/json" })
	public String validate(@RequestBody String payload) {
		return invoiceService.validateInvoice(payload);
	}

	@GetMapping("g")
	public JSONObject g() {
		JSONObject jo = new JSONObject();
		jo.put("response", "success");
		return jo;
	}

}
