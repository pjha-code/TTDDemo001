package com.ttd.demo001.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CTR_CLOUD_REQUEST_LOGS")
@Getter
@Setter
@NoArgsConstructor
public class CTRCloudRequestLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;
	@ManyToOne
	private CTRInvoiceServiceLog ctrInvoiceServiceLog;
	private String requestType;
	private String requestMethod;
	private byte[] requestPayload;
	private Short responseCode;
	private byte[] responsePayload;
	private Date creationDate = new Date();
	private String createdBy;
	private Date lastUpdateDate = new Date();
	private String updatedBy;

	public CTRCloudRequestLog(CTRInvoiceServiceLog ctrInvoiceServiceLog, String requestType, String requestMethod,
			String requestPayload, String createdBy, String updatedBy) {
		super();
		this.ctrInvoiceServiceLog = ctrInvoiceServiceLog;
		this.requestType = requestType;
		this.requestMethod = requestMethod;
		this.requestPayload = requestPayload != null ? requestPayload.getBytes() : null;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public void setRequestPayload(String payload) {
		this.requestPayload = payload != null ? payload.getBytes() : null;
	}

	public String getRequestPayload() {
		return requestPayload != null ? new String(requestPayload) : null;
	}

	public void setResponsePayload(String payload) {
		this.responsePayload = payload != null ? payload.getBytes() : null;
	}

	public String getResponsePayload() {
		return responsePayload != null ? new String(responsePayload) : null;
	}

}
