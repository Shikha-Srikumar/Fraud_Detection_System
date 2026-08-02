package com.cdac.fraud.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
public class RetrainingService {

    @Async // Runs in a separate background thread
    public void triggerPythonRetraining() {
        System.out.println(" Background Thread Started: Initiating ML Retraining Pipeline...");

        try {
            // 1. Define the command to run Python
            // NOTE: Use "python3" instead of "python" if you are on Linux/Mac
            ProcessBuilder processBuilder = new ProcessBuilder("python", "mock_retrain.py");

            // 2. Point it to the folder where your Python scripts live
            // Adjust this path to match where your GitHub repository folder is
            processBuilder.directory(new File("\"U:\\home\\samvas\\Fraud_Detection_System\\retraining-pipeline\\src\\main\\java\\com\\cdac\\fraud\\service\""));
    
            // 3. Start the process
            Process process = processBuilder.start();

            // 4. Capture the Python console output (Standard Output)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON-ML] " + line);
            }

            // 5. Capture any Python errors (Standard Error)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println("[PYTHON-ERROR] " + line);
            }

            // 6. Wait for the Python script to finish and get exit code
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println(" Retraining Complete! New Challenger Model is ready.");
                // TODO later: Trigger the Champion-Challenger DB comparison here
            } else {
                System.err.println(" Retraining Failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to launch Python script.");
            e.printStackTrace();
        }
    }
}