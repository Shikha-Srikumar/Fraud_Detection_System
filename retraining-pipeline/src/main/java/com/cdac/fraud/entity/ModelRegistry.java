package com.cdac.fraud.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "model_registry")
public class ModelRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelVersion; // e.g., "Champion_v1", "Challenger_v2"

    private LocalDateTime trainingTimestamp;

    // Classification Metrics from Sneha/Your Retraining Phase
    private double f1Score;
    private double auroc;
    private double accuracy;
    
    @Column(length = 500)
    private String affectedFeaturesTrigger; // What caused this retraining?

    @Column(nullable = false)
    private boolean isPromotedToChampion; // Did it beat the old model?
}