package com.cdac.fraud.dto;

import lombok.Data;
import java.util.List;

@Data
public class DriftAlertDTO {
	private boolean driftDetected;
    private double psiValue;
    private String driftTimestamp;
    private List<String> affectedFeatures;
	public double getPsiValue() {
		return psiValue;
	}
	public void setPsiValue(double psiValue) {
		this.psiValue = psiValue;
	}
	public String getDriftTimestamp() {
		return driftTimestamp;
	}
	public void setDriftTimestamp(String driftTimestamp) {
		this.driftTimestamp = driftTimestamp;
	}
	public List<String> getAffectedFeatures() {
		return affectedFeatures;
	}
	public void setAffectedFeatures(List<String> affectedFeatures) {
		this.affectedFeatures = affectedFeatures;
	}
	public void setDriftDetected(boolean driftDetected) {
		this.driftDetected = driftDetected;
	}
	public boolean isDriftDetected() {
		// TODO Auto-generated method stub
		return driftDetected;
	}
}
