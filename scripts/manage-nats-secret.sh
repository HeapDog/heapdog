#!/bin/bash
set -e

ACTION=$1
NS=$2
SECRET_NAME="nats-secret"
TMP_FILE="/tmp/nats-auth.conf"

TEMPLATE='authorization {
  users = [
    { user: "app1", password: "app1pass" }
    { user: "app2", password: "app2pass" }
  ]
}'

# Ensure namespace exists
if ! kubectl get ns "$NS" >/dev/null 2>&1; then
    echo "Namespace $NS does not exist. Creating it..."
    kubectl create ns "$NS"
fi

if [ "$ACTION" == "create" ]; then
    if kubectl get secret "$SECRET_NAME" -n "$NS" >/dev/null 2>&1; then
        echo "Secret $SECRET_NAME already exists in $NS."
        exit 1
    fi

    echo "$TEMPLATE" > "$TMP_FILE"
    vim "$TMP_FILE"
    
    # Check if file is empty or unchanged? User didn't ask.
    
    kubectl create secret generic "$SECRET_NAME" -n "$NS" --from-file=auth.conf="$TMP_FILE"
    echo "Secret created."

elif [ "$ACTION" == "update" ]; then
    if ! kubectl get secret "$SECRET_NAME" -n "$NS" >/dev/null 2>&1; then
        echo "Error: Secret $SECRET_NAME not found in $NS."
        exit 1
    fi

    # Decode existing
    kubectl get secret "$SECRET_NAME" -n "$NS" -o jsonpath='{.data.auth\.conf}' | base64 -d > "$TMP_FILE"
    
    vim "$TMP_FILE"
    
    kubectl create secret generic "$SECRET_NAME" -n "$NS" --from-file=auth.conf="$TMP_FILE" --dry-run=client -o yaml | kubectl apply -f -
    echo "Secret updated."
fi
rm -f "$TMP_FILE"

