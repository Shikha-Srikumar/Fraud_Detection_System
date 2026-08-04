package com.cdac.fraud.dto;

@Data
public class DashboardDTO {
	private String dataDrift;
    private String conceptDrift;
    private String modelHealth;
    private boolean retrainingRequired;
	public String getDataDrift() {
		return dataDrift;
	}
	public void setDataDrift(String dataDrift) {
		this.dataDrift = dataDrift;
	}
	public String getConceptDrift() {
		return conceptDrift;
	}
	public void setConceptDrift(String conceptDrift) {
		this.conceptDrift = conceptDrift;
	}
	public String getModelHealth() {
		return modelHealth;
	}
	public void setModelHealth(String modelHealth) {
		this.modelHealth = modelHealth;
	}
	public boolean isRetrainingRequired() {
		return retrainingRequired;
	}
	public void setRetrainingRequired(boolean retrainingRequired) {
		this.retrainingRequired = retrainingRequired;
	}
}
