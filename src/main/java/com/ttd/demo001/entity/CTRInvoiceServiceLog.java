package com.ttd.demo001.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CTR_INVOICE_SERVICE_LOGS")
@Getter
@Setter
@NoArgsConstructor
public class CTRInvoiceServiceLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recordId;
	private String serviceName;
	private byte[] payload;
	private Date creationDate = new Date();
	private String createdBy;
	private Date lastupdateDate = new Date();
	private String updatedBy;
	private String serviceType = "INVOICE";
	private String serviceSubType;
	private Short recordCount;

	public CTRInvoiceServiceLog(String serviceName, String payload, String updatedBy, String serviceSubtype,
			Short recordCount, String createdBy) {
		super();
		this.serviceName = serviceName;
		this.payload = payload != null ? payload.getBytes() : null;
		this.updatedBy = updatedBy;
		this.createdBy = createdBy;
		this.serviceSubType = serviceSubtype;
		this.recordCount = recordCount;
	}

	public String getPayload() {
		return new String(payload);
	}

}
