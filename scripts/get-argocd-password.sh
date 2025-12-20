#!/bin/bash
set -e

echo "Fetching ArgoCD admin password..."

if kubectl -n argocd get secret argocd-initial-admin-secret >/dev/null 2>&1; then
    PASS=$(kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d)
    echo "------------------------------------------------"
    echo "User: admin"
    echo "Password: $PASS"
    echo "------------------------------------------------"
else
    echo "Error: 'argocd-initial-admin-secret' not found in namespace 'argocd'."
    echo "The initial password secret might have been deleted after the first login or the installation method differs."
fi

