#!/bin/bash
set -e

NS=$1

if [ -z "$NS" ]; then
    echo "Usage: $0 <namespace>"
    exit 1
fi

if [ "$NS" == "argocd" ]; then
    echo "Error: Cannot destroy 'argocd' namespace with this script."
    exit 1
fi

echo "================================================================"
echo "WARNING: This will DESTROY the entire environment '$NS'."
echo "================================================================"
echo "Actions to be performed:"
echo "1. Delete ArgoCD Application 'root-app-$NS' in 'argocd' namespace."
echo "2. Delete namespace '$NS' (removes all Secrets, ConfigMaps, Deployments, PVCs, Jobs, etc.)"
echo "3. Kill any local 'kubectl port-forward' processes targeting namespace '$NS'."
echo ""

read -p "Are you sure you want to proceed? (y/N): " confirm
if [[ "$confirm" != "y" && "$confirm" != "Y" ]]; then
    echo "Aborted."
    exit 0
fi

echo ""
echo "Step 1: Deleting ArgoCD root application..."
# We use --cascade=foreground so we wait for dependents to be deleted if possible,
# though deleting the App usually triggers pruning.
kubectl delete application "root-app-$NS" -n argocd --ignore-not-found

echo ""
echo "Step 2: Deleting namespace $NS..."
kubectl delete ns "$NS" --ignore-not-found

echo ""
echo "Step 3: Stopping local port-forwarding..."
# pkill -f matches the full command line.
# We look for kubectl port-forward commands targeting this namespace.
# We try to match "-n NS", "-n=NS", "--namespace NS", "--namespace=NS"
# A simple generic grep might be safer to catch variants.
count=$(pgrep -f "kubectl.*port-forward.*$NS" | wc -l)
if [ "$count" -gt 0 ]; then
    pkill -f "kubectl.*port-forward.*$NS"
    echo "Stopped $count port-forward process(es)."
else
    echo "No matching port-forwarding processes found."
fi

echo ""
echo "Environment $NS destroyed successfully."

