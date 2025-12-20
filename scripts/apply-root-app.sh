#!/bin/bash
set -e

NS=$1
ROOT_APP="argocd/bootstrap/root-app-${NS}.yaml"

if [ -f "$ROOT_APP" ]; then
    echo "Applying root application for environment: $NS"
    kubectl apply -f "$ROOT_APP"
else
    echo "Error: Root app manifest $ROOT_APP not found for environment $NS."
    echo "Expected file at: $ROOT_APP"
    exit 1
fi

