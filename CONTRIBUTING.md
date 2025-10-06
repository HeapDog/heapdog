# Contributing to HeapDog

This document provides guidelines to help maintain a clear and efficient workflow for all contributors. Please read and follow these guidelines when contributing to the repository. 🚀

---

## 🧭 Branch Naming Convention

🌟 We use a consistent branch naming structure to ensure clarity and organization. Below is the naming convention for different types of branches:

| **Branch Type**  | **Format**                           | **Branch Origin**    | **Description**                                            |
| ---------------- | ------------------------------------ | -------------------- | ---------------------------------------------------------- |
| **Main**         | `main`                               | - (root branch)      | Stable branch containing production-ready code.            |
| **Development**  | `dev`                                | `main`               | Central branch for integrating ongoing development.        |
| **Feature**      | `feature/<user>/<short-description>` | `dev`                | For developing new features or enhancements.               |
| **Fix**          | `fix/<user>/<short-description>`     | `dev`                | For resolving non-critical bugs or issues.                 |
| **Hotfix**       | `hotfix/<short-description>`         | `main`               | For addressing critical issues in production.              |
| **Chore**        | `chore/<user>/<short-description>`   | `dev`                | For refactoring, dependency updates, or other minor tasks. |
| **Release**      | `release/<version>`                  | `dev`                | Used to prepare code for production deployment.            |
| **Experimental** | `experiment/<short-description>`     | `dev` or independent | For testing new ideas or proof-of-concept implementations. |

---

## 🤩 Pull Request (PR) Guidelines

* Always branch off from `dev` unless you are creating a **hotfix** from `main`.
* Keep PRs **small and focused**—ideally one logical change per PR.
* Provide a **clear title and concise description** of what the PR does.
* Link related issues using keywords (e.g., `Fixes #42`).
* Ensure your branch is **up-to-date with `dev`** before submitting a PR.
* Add relevant **unit/integration tests** for any new functionality or fixes.
* All PRs must pass CI checks (linting, formatting, tests) before merging.

---

## 🧪 Code Standards

* Follow the existing **code style** and structure.
* Ensure **consistent naming** for classes, variables, and methods.
* Document public methods, endpoints, and major architectural decisions.
* Avoid committing commented-out or dead code.
* Prefer **descriptive commit messages**, e.g.:

  ```
  feat(user): add JWT-based authentication
  fix(db): resolve connection leak in user repository
  chore(ci): update GitHub Actions build pipeline
  ```

---

## 🧼 Commit Message Format

Follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```
<type>(<scope>): <short summary>
```

**Common types:**

* `feat` – New feature or enhancement
* `fix` – Bug fix
* `chore` – Maintenance or tooling changes
* `docs` – Documentation updates
* `refactor` – Code change that neither fixes a bug nor adds a feature
* `test` – Adding or updating tests
* `ci` – Continuous integration or deployment-related changes

---

## 🧱 Development Workflow

1. **Fork** the repository (if external contributor).
2. **Clone** your fork and create a branch from `dev`:

   ```bash
   git checkout dev
   git pull origin dev
   git checkout -b feature/<user>/<short-description>
   ```
3. **Implement** your changes.
4. **Run tests and linters** to ensure code quality.
5. **Commit** with a meaningful message.
6. **Push** your branch and open a **Pull Request** against `dev`.
7. Wait for **review and approval** before merging.

---

## 🧮 Additional Notes

* Avoid making direct commits to `main` or `dev`.
* Large or structural changes should first be proposed via a **discussion or issue**.
* Tag your PR as `feature`, `bug`, `chore`, or `refactor` to improve visibility.
* Write tests for all critical logic to maintain stability.
* Ensure no sensitive data (tokens, keys, passwords) is committed.

---

## 💬 Questions or Suggestions?

Open a [GitHub Discussion](../../discussions) or reach out via an issue.
Your feedback and contributions help make **HeapDog** better for everyone. ❤️
