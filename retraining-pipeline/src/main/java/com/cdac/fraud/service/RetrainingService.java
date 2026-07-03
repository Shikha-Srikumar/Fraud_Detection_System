package com.cdac.fraud.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

@Service
public class RetrainingService {

    public void triggerOfflineRetraining(double psiValue, String features) {
        System.out.println("--- INITIATING ASYNC PYTHON RETRAINING PIPELINE ---");
        
        try {
            // Path to where your team's Python scripts are stored
            // Note: Adjust the working directory path to match your local setup
            String pythonScriptPath = "src/retrain.py"; 
            
            ProcessBuilder processBuilder = new ProcessBuilder(
                "python", 
                pythonScriptPath, 
                "--psi", String.valueOf(psiValue),
                "--features", features
            );
            
            // Set the working directory to your Python src folder
            // processBuilder.directory(new File("C:/path/to/Fraud_Detection_System-WebApp/src"));
            
            processBuilder.redirectErrorStream(true); // Merge errors with standard output
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON LOG]: " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("SUCCESS: Challenger Model trained and saved.");
                // Here you would eventually read the JSON output from Python and save to ModelRegistry DB
            } else {
                System.err.println("FAILURE: Python script crashed with exit code " + exitCode);
                // Graceful degradation: System continues using the old Champion model
            }

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to execute ProcessBuilder.");
            e.printStackTrace();
        }
    }
}