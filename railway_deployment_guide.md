# Railway Deployment Guide for SSB Application

This guide outlines the steps to deploy your application components to Railway.

## Prerequisites
- A [Railway](https://railway.app/) account.
- The [Railway CLI](https://docs.railway.app/guides/cli) or GitHub access.

## 1. Create a New Project
1. Go to your Railway Dashboard.
2. Click **"New Project"**.
3. Select **"Deploy from GitHub repo"** and choose your repository.

## 2. Configure Services

### A. Database Service (Postgres)
1. In your project canvas, create a **PostgreSQL** service.
2. Note the connection variables (`DATABASE_URL`, `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`, `PGDATABASE`).

### B. Database Service (MongoDB)
1. Create a **MongoDB** service.
2. Note the `MONGO_URL`.

### C. Keycloak Service
1. Add a **GitHub Repo** service for Keycloak.
2. Configure:
   - **Root Directory**: `/`
   - **DockerfilePath**: `docker-services/Dockerfile.keycloak`
   - **Variables**:
     - `KC_DB`: `postgres`
     - `KC_DB_URL`: `jdbc:postgresql://${{PostgreSQL.PGHOST}}:${{PostgreSQL.PGPORT}}/${{PostgreSQL.PGDATABASE}}`
     - `KC_DB_USERNAME`: `${{PostgreSQL.PGUSER}}`
     - `KC_DB_PASSWORD`: `${{PostgreSQL.PGPASSWORD}}`
     - `KEYCLOAK_ADMIN`: `admin`
     - `KEYCLOAK_ADMIN_PASSWORD`: (your choice)
     - `KC_PROXY`: `edge`

### D. News Service
1. Add a **GitHub Repo** service.
2. Configure:
   - **Root Directory**: `News Section`
   - **DockerfilePath**: `News Section/Dockerfile`

### E. Analyse Service
1. Add a **GitHub Repo** service.
2. Configure:
   - **Root Directory**: `Analyse`
   - **DockerfilePath**: `Analyse/Dockerfile`

### F. PPDT Backend
1. Add a **GitHub Repo** service.
2. Configure:
   - **Root Directory**: `PPDT`
   - **DockerfilePath**: `PPDT/Dockerfile`
   - **Variables**:
     - `SPRING_PROFILES_ACTIVE`: `prod`
     - `DB_HOST`: `${{PostgreSQL.PGHOST}}`
     - `DB_PORT`: `${{PostgreSQL.PGPORT}}`
     - `DB_NAME`: `${{PostgreSQL.PGDATABASE}}`
     - `DB_USER`: `${{PostgreSQL.PGUSER}}`
     - `DB_PASS`: `${{PostgreSQL.PGPASSWORD}}`
     - `MONGO_URI`: `${{MongoDB.MONGO_URL}}`
     - `KEYCLOAK_ISSUER_URI`: `https://<YOUR-KEYCLOAK-DOMAIN>/realms/ssb-realm`
     - `KEYCLOAK_BASE_URL`: `https://<YOUR-KEYCLOAK-DOMAIN>`
     - `NEWS_SERVICE_URL`: `http://${{news-service.RAILWAY_PRIVATE_DOMAIN}}:8000/news`
     - `ANALYSE_SERVICE_URL`: `http://${{analyse-service.RAILWAY_PRIVATE_DOMAIN}}:8001/analyze/ppdt`

## 3. Frontend
Deploy your frontend and set environment variables to point to the PPDT Backend public URL.
