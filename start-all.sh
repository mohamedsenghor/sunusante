#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="${ROOT_DIR}/logs"
DEPLOY_FILE="${ROOT_DIR}/sunusante-gateway-service/src/main/docker/deploy.yml"

mkdir -p "${LOG_DIR}"

echo "==> Demarrage infrastructure Docker (Registry, Kafka, Zookeeper)..."
docker compose -f "${DEPLOY_FILE}" up -d

run_service() {
  local service_dir="$1"
  local log_file="$2"

  echo "==> Demarrage ${service_dir}..."
  (
    cd "${ROOT_DIR}/${service_dir}"
    nohup ./mvnw -DskipTests -Dspring-boot.run.profiles=dev spring-boot:run > "${LOG_DIR}/${log_file}" 2>&1 &
  )
}

run_service "sunusante-patient-service" "patient.log"
run_service "sunusante-dmp-service" "dmp.log"
run_service "sunusante-audit-service" "audit.log"
run_service "sunusante-gateway-service" "gateway.log"

echo ""
echo "Demarrage lance."
echo "Logs: Consulter le dossier ${LOG_DIR} pour voir les logs de chaque service lors du démarrage."
echo "Gateway: http://localhost:9040"
echo "Patient: http://localhost:9041"
echo "DMP: http://localhost:9042"
echo "Audit: http://localhost:9043"
echo "Registry: http://localhost:8761"

