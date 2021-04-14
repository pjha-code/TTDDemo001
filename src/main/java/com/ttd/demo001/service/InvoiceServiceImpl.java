package com.ttd.demo001.service;

import java.net.HttpURLConnection;
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

	@SuppressWarnings("unchecked")
	@Override
	public String updateInvoice(Map<Integer, String> records) {

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
						ctrCloudRequestLog2.setResponsePayload(updateInvoiceResp);
						cloudRequestLoggerRepo.save(ctrCloudRequestLog2);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				});
			}

		});

		return null;
	}

	@Override
	public String getAllInvoices() {
		CTRInvoiceServiceLog serviceLog = new CTRInvoiceServiceLog(InvoiceServiceName.GET_ALL_INVOICE, null, updatedBy,
				null, (short) 0, createdBy);
		invoiceServiceLoggerRepo.save(serviceLog);

		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_GET, resourceUrl);
		CTRCloudRequestLog cloudRequestLog = new CTRCloudRequestLog(serviceLog, "INVOICE", HTTPMethods.HTTP_GET, null,
				createdBy, updatedBy);
		cloudRequestLoggerRepo.save(cloudRequestLog);
		return invoiceRespGenService.getResponsePayload(conn1, cloudRequestLog);
	}

	@Override
	public String getSingleInvoice(String invoiceNumber) {
		CTRInvoiceServiceLog serviceLog = new CTRInvoiceServiceLog(InvoiceServiceName.GET_SINGLE_INVOICE, invoiceNumber,
				updatedBy, null, (short) 0, createdBy);
		invoiceServiceLoggerRepo.save(serviceLog);

		String updatedResourceURL = resourceUrl + "/" + invoiceNumber;
		HttpURLConnection conn1 = conn.getConnection(HTTPMethods.HTTP_GET, updatedResourceURL);
		CTRCloudRequestLog cloudRequestLog = new CTRCloudRequestLog(serviceLog, "INVOICE", HTTPMethods.HTTP_GET,
				invoiceNumber, createdBy, updatedBy);
		cloudRequestLoggerRepo.save(cloudRequestLog);
		return invoiceRespGenService.getResponsePayload(conn1, cloudRequestLog);
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
