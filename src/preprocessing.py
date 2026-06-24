import numpy as np
import pandas as pd
from sklearn.base import BaseEstimator, TransformerMixin
from sklearn.preprocessing import StandardScaler

class AmountTimeScaler(BaseEstimator, TransformerMixin):
    """Standardize only Amount (+ optionally Time); leave V1..V28 unchanged."""
    def __init__(self, scale_time=True):
        self.scale_time = scale_time
        self.scaler_amount = StandardScaler()
        self.scaler_time = StandardScaler() if scale_time else None

    def fit(self, x, y=None):
        self.scaler_amount.fit(x[["amount"]])
        if self.scale_time:
            ts = pd.to_datetime(x["timestamp"], format="mixed")
            ts_numeric = ts.astype("int64").to_frame()
            self.scaler_time.fit(ts_numeric)
        return self

    def transform(self, x):
        x = x.copy()
        x["amount"] = self.scaler_amount.transform(x[["amount"]])
        if self.scale_time:
            ts = pd.to_datetime(x["timestamp"], format="mixed")
            ts_numeric = ts.astype("int64").to_frame()
            x["timestamp"] = self.scaler_time.transform(ts_numeric)
        return x

def split_x_y(df, label_col="is_fraud"):
    y = df[label_col].astype(int).values
    x = df.drop(columns=[label_col])
    return x, y


def preprocess_features(x):

    x = x.copy()

    # Drop high-cardinality identifier columns
    high_cardinality_cols = [
        "transaction_id",
        "sender_account",
        "receiver_account",
        "ip_address",
        "device_hash"
    ]

    x = x.drop(columns=high_cardinality_cols, errors="ignore")

    # Timestamp features
    if "timestamp" in x.columns:

        x["timestamp"] = pd.to_datetime(
            x["timestamp"],
            format="ISO8601",
            errors="coerce"
        )

        x["hour"] = x["timestamp"].dt.hour
        x["day_of_week"] = x["timestamp"].dt.dayofweek
        x["is_weekend"] = (
            x["day_of_week"] >= 5
        ).astype(int)

        x = x.drop(columns=["timestamp"], errors="ignore")

    # Drop fraud subtype (not available at prediction time)
    x = x.drop(columns=["fraud_type"], errors="ignore")

    # Categorical columns
    categorical_cols = [
        "transaction_type",
        "merchant_category",
        "location",
        "device_used",
        "payment_channel"
    ]

    existing_cats = [
        col for col in categorical_cols
        if col in x.columns
    ]

    x = pd.get_dummies(
        x,
        columns=existing_cats,
        drop_first=True
    )

    return x