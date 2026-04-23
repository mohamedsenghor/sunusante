#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEPLOY_FILE="${ROOT_DIR}/sunusante-gateway-service/src/main/docker/deploy.yml"

# Ports used by gateway/microservices and gateway UI in this workspace.
PORTS=(9040 9041 9042 9043 9060)

kill_by_port() {
  local port="$1"
  local pids
  pids="$(lsof -ti :"${port}" 2>/dev/null || true)"
  if [[ -n "${pids}" ]]; then
    echo "Killing processes on port ${port}: ${pids}"
    kill ${pids} 2>/dev/null || true
    sleep 1
    # Force kill if still running.
    pids="$(lsof -ti :"${port}" 2>/dev/null || true)"
    if [[ -n "${pids}" ]]; then
      kill -9 ${pids} 2>/dev/null || true
    fi
  else
    echo "No process found on port ${port}"
  fi
}

echo "==> Stopping local Java/Node services..."
for port in "${PORTS[@]}"; do
  kill_by_port "${port}"
done

echo "==> Stopping Docker infrastructure..."
if [[ -f "${DEPLOY_FILE}" ]]; then
  docker compose -f "${DEPLOY_FILE}" down || docker-compose -f "${DEPLOY_FILE}" down
else
  echo "deploy.yml not found at: ${DEPLOY_FILE}"
fi

echo "Done."

