package com.ttd.demo001.service;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ttd.demo001.entity.CTRCloudRequestLog;
import com.ttd.demo001.entity.CTRInvoiceServiceLog;
import com.ttd.demo001.repository.CTRCloudRequestLogRepository;
import com.ttd.demo001.repository.CTRInvoiceServiceLogRepository;
import com.ttd.demo001.util.HTTPMethods;
import com.ttd.demo001.util.InvoiceServiceName;
import com.ttd.demo001.util.JSONHelper;

@Service
@SuppressWarnings("unchecked")
public class InvoiceServiceImpl implements InvoiceService {

	@Value("${resource_url}")
	private String resourceUrl;
	@Autowired
	private CTRInvoiceServiceLogRepository invoiceServiceLoggerRepo;
	@Autowired
	private CTRCloudRequestLogRepository cloudRequestLoggerRepo;
	@Autowired
	private InvoiceResponseGeneratorService invoiceRespGenService;
	@Value("${created_by}")
	private String createdBy;
	@Value("${updated_by}")
	private String updatedBy;
	@Autowired
	private com.ttd.demo001.util.HttpURLConnection conn;
	@Autowired
	private JSONParser parser;

	@Override
	public JSONObject updateInvoice(Map<Integer, String> records) {
		JSONObject respoObject = new JSONObject();
		CTRInvoiceServiceLog serviceLog = new CTRInvoiceServiceLog(InvoiceServiceName.UPDATE_INVOICE, null, updatedBy,
				null, (short) records.size(), createdBy);
		invoiceServiceLoggerRepo.save(serviceLog);

		records.keySet().stream().forEach((recordKey) -> {
//			System.out.println(records.get(recordKey));
			if (recordKey != 0) {
				JSONObject recordValue = JSONHelper.createJSONString(records.get(0), records.get(recordKey));
//				System.out.println(recordValue);
				CTRCloudRequestLog cloudRequestLog = new CTRCloudRequestLog(serviceLog, "INVOICE", HTTPMethods.HTTP_GET,
						null, createdBy, updatedBy);

				recordValue.keySet().stream().forEach((key) -> {
					try {
						HttpURLConnection conn2 = conn.getConnection(HTTPMethods.HTTP_GET,
								resourceUrl + "?q=InvoiceNumber=" + key);
						cloudRequestLog.setRequestPayload("?q=InvoiceNumber=" + key);

						String getInvoiceResp = invoiceRespGenService.getResponsePayload(conn2, cloudRequestLog);
						JSONObject invoiceJsonObject = (JSONObject) parser.parse(getInvoiceResp);
						cloudRequestLog.setResponsePayload(invoiceJsonObject.toJSONString());
						cloudRequestLoggerRepo.save(cloudRequestLog);
						JSONArray itemsArray = (JSONArray) invoiceJsonObject.get("items");
						JSONObject firstItem = (JSONObject) itemsArray.get(0);

						String invoiceId = firstItem.get("InvoiceId").toString();
						JSONObject updateServicePayload = (JSONObject) recordValue.get(key);

						CTRCloudRequestLog ctrCloudRequestLog2 = new CTRCloudRequestLog(serviceLog, "UPDATE INVOICE",
								HTTPMethods.HTTP_POST, updateServicePayload.toJSONString(), createdBy, updatedBy);
						cloudRequestLoggerRepo.save(ctrCloudRequestLog2);

						HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_POST,
								resourceUrl + "/" + invoiceId);
						conn1.setRequestProperty("Accept", "application/vnd.oracle.adf.resourceitem+json");
						conn1.setRequestProperty("X-HTTP-Method-Override", "PATCH");
						String updateInvoiceResp = invoiceRespGenService
								.setRequestPayloadForResponse(recordValue.get(key).toString(), conn1, cloudRequestLog);
						respoObject.put("response", parser.parse(updateInvoiceResp));
						ctrCloudRequestLog2.setResponsePayload(updateInvoiceResp);
						cloudRequestLoggerRepo.save(ctrCloudRequestLog2);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				});
			}

		});

		return respoObject;
	}

	@Override
	public List<Map<String, String>> getAllInvoices() {
		JSONObject resp = new JSONObject();
		resp.put("error", "server response never received");
		CTRInvoiceServiceLog serviceLog = new CTRInvoiceServiceLog(InvoiceServiceName.GET_ALL_INVOICE, null, updatedBy,
				null, (short) 0, createdBy);
		invoiceServiceLoggerRepo.save(serviceLog);

		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_GET, resourceUrl + "?limit=5");
		CTRCloudRequestLog cloudRequestLog = new CTRCloudRequestLog(serviceLog, "INVOICE", HTTPMethods.HTTP_GET, null,
				createdBy, updatedBy);
		cloudRequestLoggerRepo.save(cloudRequestLog);

		String respPayload = invoiceRespGenService.getResponsePayload(conn1, cloudRequestLog);
		System.out.println(respPayload);

		try {
			resp = (JSONObject) parser.parse(respPayload);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Map<String, String>> tempList = new LinkedList<>();
		for (Object jo : (JSONArray) resp.get("items")) {
			JSONObject temp = (JSONObject) jo;
			Map<String, String> t = new HashMap<>();
			t.put("InvoiceNumber", (String) temp.get("InvoiceNumber"));
			t.put("CreationDate", (String) temp.get("CreationDate"));
			t.put("CreatedBy", (String) temp.get("CreatedBy"));
			tempList.add(t);
		}

		return tempList;
	}

	@Override
	public JSONObject getSingleInvoice(String invoiceNumber) {
		JSONObject responseObject = null;
		CTRInvoiceServiceLog serviceLog = new CTRInvoiceServiceLog(InvoiceServiceName.GET_SINGLE_INVOICE, invoiceNumber,
				updatedBy, null, (short) 0, createdBy);
		invoiceServiceLoggerRepo.save(serviceLog);

		String updatedResourceURL = resourceUrl + "?q=InvoiceNumber=" + invoiceNumber;
		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_GET, updatedResourceURL);
		CTRCloudRequestLog cloudRequestLog = new CTRCloudRequestLog(serviceLog, "INVOICE", HTTPMethods.HTTP_GET,
				invoiceNumber, createdBy, updatedBy);
		cloudRequestLoggerRepo.save(cloudRequestLog);
		try {
			responseObject = (JSONObject) parser.parse(invoiceRespGenService.getResponsePayload(conn1, cloudRequestLog));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return responseObject;
	}
	
	@Override
	public String validateInvoice(String requestObject) {
		CTRInvoiceServiceLog serviceLog = new CTRInvoiceServiceLog(InvoiceServiceName.VALIDATE_INVOICE, null, updatedBy,
				null, (short) 0, createdBy);
		invoiceServiceLoggerRepo.save(serviceLog);

		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_POST, resourceUrl);
		conn1.setRequestProperty("Accept", "application/vnd.oracle.adf.action+json");

		CTRCloudRequestLog cloudRequestLog = new CTRCloudRequestLog(serviceLog, "INVOICE", HTTPMethods.HTTP_POST,
				requestObject, createdBy, updatedBy);
		String resp = invoiceRespGenService.setRequestPayloadForResponse(requestObject, conn1, cloudRequestLog);
		cloudRequestLog.setResponsePayload(resp);
		cloudRequestLoggerRepo.save(cloudRequestLog);
		return resp;
	}

}
