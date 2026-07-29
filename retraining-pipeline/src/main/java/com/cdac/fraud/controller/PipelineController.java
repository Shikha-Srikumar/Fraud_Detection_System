package com.cdac.fraud.controller;

import com.cdac.fraud.entity.DriftReport;
import com.cdac.fraud.repository.DriftReportRepository;
import com.cdac.fraud.service.RetrainingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Allowing Moksha's UI to fetch data
public class PipelineController {

    @Autowired
    private DriftReportRepository reportRepository;
    
    @Autowired
    private RetrainingService retrainingService;

    /**
     * 1. GYANVI'S ENDPOINT (Python -> Spring Boot)
     * Receives the raw JSON payload and saves it directly to PostgreSQL JSONB.
     */
    @PostMapping("/pipeline/analyze-drift")
    public ResponseEntity<String> GetsaveDriftReport(@RequestBody String rawJsonPayload) {
        
    	// 1. Save to database
    	DriftReport newReport = new DriftReport();
    	newReport.setGeneratedAt(LocalDateTime.now());
    	newReport.setPayload(rawJsonPayload);
    	reportRepository.save(newReport);
    	System.out.println("Success: New drift report logged to PostgreSQL.");
   

        // NOTE: Later, we will add the ProcessBuilder logic here to trigger 
        // retraining if the JSON payload says "retrainingRequired": true
        
        
    	// 2. Evaluate if retraining is needed
    	// Quick string check to avoid complex JSON parsing for now
    	if (rawJsonPayload.contains("\"retrainingRequired\": true")) {
    		System.out.println(" DRIFT ALERT: Retraining flag is true. Triggering engine...");
    		
    		// Call the async service
    		retrainingService.triggerPythonRetraining();
    		
    		return ResponseEntity.ok("Drift report logged. Concept drift detected! Offline retraining initiated in background.");
    	}
    	
    	return ResponseEntity.ok("Drift report logged. Retraining skipped per ML engine recommendation.");
    	
    }
    
    /**
     * 2. MOKSHA'S ENDPOINT (Spring Boot -> React/UI)
     * Fetches the latest JSONB report and serves it to the Dashboard charts.
     */
    @GetMapping("/dashboard/latest-report")
    public ResponseEntity<String> getLatestReport() {
        
        Optional<DriftReport> latestReport = reportRepository.findTopByOrderByGeneratedAtDesc();
        
        if (latestReport.isPresent()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(latestReport.get().getPayload());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}