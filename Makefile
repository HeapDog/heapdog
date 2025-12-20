# Allow usage of lowercase ns variable
ifdef ns
NS := $(ns)
endif

NS ?= prod

.PHONY: db-secret update-db-secret nats-secret update-nats-secret argocd argocd-password apply-root-app setup destroy

db-secret:
	@chmod +x scripts/manage-db-secret.sh
	@./scripts/manage-db-secret.sh create $(NS)

update-db-secret:
	@chmod +x scripts/manage-db-secret.sh
	@./scripts/manage-db-secret.sh update $(NS)

nats-secret:
	@chmod +x scripts/manage-nats-secret.sh
	@./scripts/manage-nats-secret.sh create $(NS)

update-nats-secret:
	@chmod +x scripts/manage-nats-secret.sh
	@./scripts/manage-nats-secret.sh update $(NS)

argocd:
	@chmod +x scripts/install-argocd.sh
	@./scripts/install-argocd.sh

argocd-password:
	@chmod +x scripts/get-argocd-password.sh
	@./scripts/get-argocd-password.sh

get-argocd-password: argocd-password


apply-root-app:
	@chmod +x scripts/apply-root-app.sh
	@./scripts/apply-root-app.sh $(NS)

setup: argocd
	@echo "Setting up secrets for namespace $(NS)..."
	@if kubectl get secret postgres-auth-secret -n $(NS) >/dev/null 2>&1; then \
		echo "postgres-auth-secret exists, updating..."; \
		$(MAKE) update-db-secret NS=$(NS); \
	else \
		echo "postgres-auth-secret missing, creating..."; \
		$(MAKE) db-secret NS=$(NS); \
	fi
	@if kubectl get secret nats-secret -n $(NS) >/dev/null 2>&1; then \
		echo "nats-secret exists, updating..."; \
		$(MAKE) update-nats-secret NS=$(NS); \
	else \
		echo "nats-secret missing, creating..."; \
		$(MAKE) nats-secret NS=$(NS); \
	fi
	@echo "Applying root app for $(NS)..."
	@$(MAKE) apply-root-app NS=$(NS)

destroy:
	@chmod +x scripts/destroy-env.sh
	@./scripts/destroy-env.sh $(NS)
