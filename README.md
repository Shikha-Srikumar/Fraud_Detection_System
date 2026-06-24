## Fraud Detection with Drift-Aware Evaluation
--------

The project addresses the challenge of detecting **rare fraudulent transactions** while monitoring **concept drift** — gradual changes in data patterns that degrade model recall over time.  
We designed a **recall-focused pipeline** that adapts dynamically to maintain consistent detection quality in streaming data.


All copyright and claims owned by the owner and collabrators of the repository along with Center for Development of Advance Computing (CDAC Bangalore).

---

## Objectives

- Develop a **binary classification** model to distinguish fraud vs. legitimate transactions.  
- Optimize for **recall**, ensuring maximum fraud capture even at moderate precision.  
- Simulate **streaming data** using time-ordered batches.  
- Measure **performance drift** via recall trends, PSI (Population Stability Index), and KS statistics.  
- Quantify the trade-off between **recall stability** and **manual review workload**.

---

## Team Members and their roles

- **Part 1: Shikha (Data & EDA Lead)**
- **Part 2: Sneha Malhotra (Baseline Model & Model Comparison Lead)**
- **Part 3: Gyanvi (Drift Simulation & Detection Lead)**
- **Part 4: Samvas (Retraining Pipeline & SpringBoot Lead)**
- **Part 5: Moksha (Threshold Recalibration & Visualization Lead)**

---

## Dataset

- **Source:** [Kaggle – Financial Transactions Dataset for Fraud Detection](https://www.kaggle.com/datasets/aryan208/financial-transactions-dataset-for-fraud-detection/data)  
  - 5000000 transactions.  
  - Features: `timestamp` and `amount`.

For this project, a **375K subset** was sampled chronologically and split into 4 batches:

| Batch | Purpose | Size | Fraud Rate |
|-------|----------|------|------------|
| batch1_train.csv | Training | 125000 | 0.03502 |
| batch2_test.csv  | Testing  | 125000 | 0.03543 |
| batch3_stream.csv | Stream 1 (Drift eval) | 125000 | 0.03590 |
| batch4_stream.csv | Stream 2 (Drift eval) | 125000 | 0.03575 |

---

## Data-Mining Pipeline

1. **Data Ingestion** – load raw and processed batches.  
2. **Preprocessing** – `AmountTimeScaler` standardizes only `amount` and `timestamp`.  
3. **Exploratory Data Analysis** – imbalance plots, histograms, scatter (Time vs Amount).  
4. **Model Training & Evaluation**
   - Models: Logistic Regression (baseline), Random Forest, XGBoost.  
   - Metrics: Precision, Recall, F1, PR-AUC.  
5. **Concept Drift Detection**
   - Compute PSI and KS statistics between Test ↔ Stream batches.  
   - Monitor recall decay over time.  
6. **Adaptive Threshold Recalibration**
   - Re-estimate decision threshold per batch to restore target recall (≈ 0.80).  
   - Compare “Fixed” vs “Adaptive” recall curves.

---

## Key Results (Checkpoint 2)


<img width="1502" height="136" alt="image" src="https://github.com/user-attachments/assets/823226d8-5f8c-47cb-b794-e1669d140305" />

<img width="1662" height="135" alt="image" src="https://github.com/user-attachments/assets/634fdf7e-9958-40e6-a8a2-6abdd164cfb3" />


**Interpretation:**
- Recall initially drops on Stream 1 due to distribution shift, but adaptive thresholding recovers performance.  
- Precision remains stable (< ±0.02), indicating manageable false positive rates.  
- Manual review load (~0.7 %) stays constant across streams.

---

## Drift Metrics Summary

| Drift Test | PSI (Amount) | PSI (Time) | KS (Score) |
|-------------|--------------|-------------|-------------|
| Test → S1 | 0.000 | 0.001 | 0.004743999999999998 |
| Test → S2 | 0.000 | 0.001 | 0.017647999999999997 |

**Interpretation:**  
- Low PSI (< 0.1) on Amount → minor distribution shift.  
- High PSI on Time → strong temporal drift (uneven transaction times).  
- Increasing KS values confirm model-score distribution shift across streams.

---


## Technologies Used

- Python 3.13 (PyCharm / Anaconda / JupyterLab)  
- Libraries: NumPy, Pandas, Scikit-Learn, XGBoost, Matplotlib  
- Version Control: Git + GitHub  
- Environment: `.venv` for reproducible dependencies

---

## Insights & Learning

- **Data imbalance** demands careful recall-precision trade-offs.  
- **Concept drift** can be quantified with PSI and KS tests.  
- **Adaptive thresholding** stabilizes recall over time without retrains.  
- **Recall-driven evaluation** aligns better with fraud prevention goals than accuracy.

---

## Future Work

- Add **ensemble comparisons** (Random Forest, XGBoost).  
- Evaluate **SMOTE vs class-weight balancing**.  
- Automate **threshold recalibration** in streaming pipeline.  
- Incorporate **real-time drift alerts** using PSI and KS thresholds. 

---


## Repository Structure

```text
fraud-detection-drift-aware/
│
├── notebooks/
│   ├── 01_setup_eda.ipynb
│   ├── 02_baseline_model.ipynb
│   ├── 03_model_compare.ipynb
│   ├── 04_imbalance_ablation.ipynb
│   ├── 05_drift_metrics.ipynb
│   └── 06_threshold_recalibration.ipynb
│
├── src/
│   ├── preprocessing.py
│   ├── sanity.py
│   └── utils.py
│
├── visuals/
│   └── eda_visuals
│
├── requirements.txt
└── README.md
```
---
