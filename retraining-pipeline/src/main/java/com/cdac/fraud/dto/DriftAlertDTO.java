package com.cdac.fraud.dto;

import lombok.Data;
import java.util.List;

@Data
public class DriftAlertDTO {
	private boolean driftDetected;
    private double psiValue;
    private String driftTimestamp;
    private List<String> affectedFeatures;
	public boolean isDriftDetected() {
		// TODO Auto-generated method stub
		return driftDetected;
	}
	public double getPsiValue() {
		// TODO Auto-generated method stub
		return psiValue;
	}
}
