package com.cdac.fraud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.fraud.dto.DriftAlertDTO;

@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {
	
	@PostMapping("/retrain")
    public ResponseEntity<String> handleDriftAlert(@RequestBody DriftAlertDTO alert) {
        if (alert.isDriftDetected()) {
            System.out.println("ALERT: Concept drift detected! PSI Value: " + alert.getPsiValue());
            
            // TODO: Call your service logic to trigger the Python script here
            
            return ResponseEntity.ok("Drift alert received. Offline retraining process initiated.");
        }
        
        return ResponseEntity.ok("Alert processed. No drift detected, skipping retraining.");
	}
}
