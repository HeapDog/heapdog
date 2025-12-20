#!/bin/bash
set -e

if kubectl get ns argocd >/dev/null 2>&1; then
    echo "Namespace argocd exists."
else
    kubectl create ns argocd
fi

if kubectl get deployment argocd-server -n argocd >/dev/null 2>&1; then
    echo "ArgoCD already installed."
else
    echo "Installing ArgoCD..."
    helm repo add argo https://argoproj.github.io/argo-helm
    helm repo update
    helm upgrade --install argocd argo/argo-cd -n argocd
fi

