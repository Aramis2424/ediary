#!/usr/bin/env bash
set -e

COMPOSE="docker-compose -f ./docker-compose.test.yml -p ediary_cicd"

UNIT_OK=false
INTEGRATION_OK=false
E2E_OK=false
FAILED=false

echo "=== Running Unit tests ==="
if $COMPOSE run --rm -e TEST_TYPE=Unit app-test; then
  UNIT_OK=true
else
  echo "Unit tests failed, skipping integration & e2e"
  FAILED=true
fi

if [ "$FAILED" = false ]; then
  echo "=== Running Integration tests ==="
  if $COMPOSE run --rm -e TEST_TYPE=Integration app-test; then
    INTEGRATION_OK=true
  else
    echo "Integration tests failed, skipping e2e"
    FAILED=true
  fi
fi

if [ "$FAILED" = false ]; then
  echo "=== Running E2E tests ==="
  if $COMPOSE run --rm -e TEST_TYPE=E2E app-test; then
    E2E_OK=true
  else
    echo "E2E tests failed"
    FAILED=true
  fi
fi

echo "=== Generating Allure report ==="
$COMPOSE run --rm -e TEST_TYPE=Allure app-test || true

echo "=== Summary ==="
echo "Unit:        $UNIT_OK"
echo "Integration: $INTEGRATION_OK"
echo "E2E:         $E2E_OK"

if [ "$FAILED" = true ]; then
  echo "One or more test stages failed"
  exit 1
else
  echo "All tests passed"
  exit 0
fi
