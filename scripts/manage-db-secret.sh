#!/bin/bash
set -e

ACTION=$1
NS=$2
VALUES_FILE="k8s/infra/postgresql/values-${NS}.yaml"

if [ ! -f "$VALUES_FILE" ]; then
    echo "Error: Values file $VALUES_FILE not found."
    exit 1
fi

SECRET_NAME=$(yq '.auth.existingSecret' "$VALUES_FILE")
if [ "$SECRET_NAME" == "null" ] || [ -z "$SECRET_NAME" ]; then
    echo "Error: auth.existingSecret not found in $VALUES_FILE"
    exit 1
fi

# Get required keys
KEYS=("postgres-password")
# Use yq to extract passwordKeys from the list
CUSTOM_KEYS=$(yq '.customSetup.databases[].passwordKey' "$VALUES_FILE")
for key in $CUSTOM_KEYS; do
    if [ "$key" != "null" ]; then
        KEYS+=("$key")
    fi
done

# Ensure namespace exists (optional but helpful)
if ! kubectl get ns "$NS" >/dev/null 2>&1; then
    echo "Namespace $NS does not exist. Creating it..."
    kubectl create ns "$NS"
fi

if [ "$ACTION" == "create" ]; then
    echo "Creating secret $SECRET_NAME in namespace $NS..."
    
    if kubectl get secret "$SECRET_NAME" -n "$NS" >/dev/null 2>&1; then
        echo "Secret $SECRET_NAME already exists in $NS. Use update command."
        exit 1
    fi

    ARGS=()
    for key in "${KEYS[@]}"; do
        read -s -p "Enter value for $key: " val
        echo ""
        ARGS+=("--from-literal=$key=$val")
    done

    kubectl create secret generic "$SECRET_NAME" -n "$NS" "${ARGS[@]}"
    echo "Secret created."

elif [ "$ACTION" == "update" ]; then
    echo "Updating secret $SECRET_NAME in namespace $NS..."

    if ! kubectl get secret "$SECRET_NAME" -n "$NS" >/dev/null 2>&1; then
        echo "Error: Secret $SECRET_NAME does not exist in $NS."
        exit 1
    fi

    PATCH_DATA="{"
    FIRST=1
    UPDATED=0
    for key in "${KEYS[@]}"; do
        read -s -p "Enter new value for $key (press enter to skip): " val
        echo ""
        if [ -n "$val" ]; then
            # Check for simple JSON injection (basic sanitization/encoding)
            # Base64 encode immediately
            encoded=$(echo -n "$val" | base64)
            
            if [ $FIRST -eq 0 ]; then PATCH_DATA+=","; fi
            PATCH_DATA+="\"$key\": \"$encoded\""
            FIRST=0
            UPDATED=1
        fi
    done
    PATCH_DATA+="}"

    if [ $UPDATED -eq 0 ]; then
        echo "No changes made."
        exit 0
    fi

    # Apply patch
    FULL_PATCH="{\"data\": $PATCH_DATA}"
    kubectl patch secret "$SECRET_NAME" -n "$NS" -p "$FULL_PATCH"
    echo "Secret updated."
fi

