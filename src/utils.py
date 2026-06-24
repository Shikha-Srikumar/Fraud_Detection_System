import numpy as np
import pandas as pd
from typing import Dict, Tuple
from sklearn.metrics import (
    precision_recall_fscore_support, average_precision_score,
    precision_recall_curve, confusion_matrix
)

# Basic metrics at a threshold
def metrics_at_threshold(y_true, y_score, thr: float) -> Dict[str, float]:
    y_pred = (y_score >= thr).astype(int)
    precision, recall, f1, _ = precision_recall_fscore_support(
        y_true, y_pred, average="binary", zero_division=0
    )
    pr_auc = average_precision_score(y_true, y_score)
    tn, fp, fn, tp = confusion_matrix(y_true, y_pred).ravel()
    flagged = int((y_pred == 1).sum())
    return {
        "threshold": thr,
        "precision": precision,
        "recall": recall,
        "f1": f1,
        "pr_auc": pr_auc,
        "TP": int(tp), "FP": int(fp), "TN": int(tn), "FN": int(fn),
        "flagged": flagged,
        "flagged_pct": flagged / len(y_true)
    }

# Choosing threshold to hit >= target recall (on a validation set)
def threshold_for_target_recall(y_true, y_score, target_recall=0.8, min_thr=1e-3, max_flagged=0.2):
    from sklearn.metrics import precision_recall_curve
    prec, rec, thr = precision_recall_curve(y_true, y_score)
    # Compute fraction flagged for each candidate threshold
    flagged_frac = [(y_score >= t).mean() for t in thr]
    best_thr = 0.5
    for r, f, t in zip(rec[1:], flagged_frac, thr):
        if r >= target_recall and f <= max_flagged and t >= min_thr:
            best_thr = float(t)
            break
    return best_thr


# Population Stability Index (PSI)
def _bin_edges(series: pd.Series, bins: int = 10):
    # Using quantile binning for robustness under skew
    return series.quantile(np.linspace(0, 1, bins + 1)).values

def psi(expected: pd.Series, actual: pd.Series, bins: int = 10) -> float:
    # expected = reference (e.g., Test), actual = new (e.g., Stream1)
    edges = _bin_edges(expected, bins=bins)
    e_counts, _ = np.histogram(expected, bins=edges)
    a_counts, _ = np.histogram(actual,   bins=edges)

    e = np.clip(e_counts / max(e_counts.sum(), 1), 1e-6, 1)
    a = np.clip(a_counts / max(a_counts.sum(), 1), 1e-6, 1)
    return float(np.sum((a - e) * np.log(a / e)))

# KS statistic for score or feature distributions
def ks_statistic(sample_a: np.ndarray, sample_b: np.ndarray) -> float:

    a = np.sort(sample_a)
    b = np.sort(sample_b)
    i = j = 0
    cdf_a = cdf_b = 0.0
    n, m = len(a), len(b)
    ks = 0.0
    while i < n and j < m:
        if a[i] <= b[j]:
            i += 1
            cdf_a = i / n
        else:
            j += 1
            cdf_b = j / m
        ks = max(ks, abs(cdf_a - cdf_b))
    return float(ks)

#  split and metrics printing 
def summarize(y_true, y_score, thr: float) -> pd.DataFrame:
    row = metrics_at_threshold(y_true, y_score, thr)
    return pd.DataFrame([row])[
        ["threshold","precision","recall","f1","pr_auc","TP","FP","TN","FN","flagged","flagged_pct"]
    ]
