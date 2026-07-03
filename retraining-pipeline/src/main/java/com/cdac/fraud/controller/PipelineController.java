package com.cdac.fraud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cdac.fraud.dto.DriftAlertDTO;
import com.cdac.fraud.service.RetrainingService;

@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {
    
    @Autowired
    private RetrainingService retrainingService;

    @PostMapping("/retrain")
    public ResponseEntity<String> handleDriftAlert(@RequestBody DriftAlertDTO alert) {
        if (alert.isDriftDetected()) {
            System.out.println("ALERT: Concept drift detected! PSI Value: " + alert.getPsiValue());
            
            // Trigger the service in a non-blocking way (or let it block for batch testing)
            String features = String.join(",", alert.getAffectedFeatures());
            retrainingService.triggerOfflineRetraining(alert.getPsiValue(), features);
            
            return ResponseEntity.ok("Drift alert received. Offline retraining process initiated.");
        }
        
        return ResponseEntity.ok("Alert processed. No drift detected, skipping retraining.");
    }
}