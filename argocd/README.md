Initial setup instructions for Argo CD
======================================================
This document provides initial setup instructions for Argo CD, a declarative, GitOps continuous delivery tool for Kubernetes.

Prerequisites
-------------
Before you begin, ensure you have the following prerequisites:
- A running Kubernetes cluster (version 1.16 or higher)
- kubectl command-line tool installed and configured to interact with your cluster
- Helm 3 installed

Installation Steps
------------------
1. **Add the Argo CD Helm repository:**
   ```bash
   helm repo add argo https://argoproj.github.io/argo-helm
   helm repo update
   ```
   
2. **Create a namespace for Argo CD:**
   ```bash
    kubectl create namespace argocd
    ```
   
3. **Install Argo CD using Helm:**
   ```bash
   helm install argocd argo/argo-cd -n argocd
   ```
4. **Verify the installation:**
   ```bash
   kubectl get pods -n argocd
   ```
   You should see several pods running in the `argocd` namespace.

5. **Access the Argo CD API server:**
   By default, the Argo CD API server is not exposed outside the cluster. You can port-forward to access it:
   ```bash
   kubectl port-forward svc/argocd-server -n argocd 8080:443
   ```
   You can now access the Argo CD UI at `https://localhost:8080`.

6. **Login to Argo CD:**
   The default username is `admin`. To get the initial password, run:
   ```bash
   kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo
   ```
   Use this password to log in to the Argo CD UI.

7. **Change the admin password (optional but recommended):**
    After logging in, navigate to the settings and change the admin password for security.

8. Now apply the `root-app` configuration to deploy your applications using Argo CD.
   ```bash
   kubectl apply -f bootstrap/root-app.yaml
   ```
   Now Argo CD will start managing the applications defined in the `root-app.yaml` file.

Congratulations! You have successfully set up Argo CD in your Kubernetes cluster. You can now start managing your applications using GitOps principles. For more information, refer to the [official Argo CD documentation](https://argo-cd.readthedocs.io/).