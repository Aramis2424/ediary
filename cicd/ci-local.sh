#!/usr/bin/env bash
set -e

COMPOSE="docker compose -f ./cicd/docker-compose.test.yml -p ediary_cicd"

BUILD_OK=false
UNIT_OK=false
INTEGRATION_OK=false
E2E_OK=false
FAILED=false

TEST_TYPE="$1"

run_build() {
  echo "=== Build application ==="
  if $COMPOSE run --rm -e TEST_TYPE=Build app-test; then
    BUILD_OK=true
  else
    echo "Building failed, skipping unit & integration & e2e"
    FAILED=true
  fi
}

run_unit() {
  echo "=== Running Unit tests ==="
  if $COMPOSE run --rm -e TEST_TYPE=Unit app-test; then
    UNIT_OK=true
  else
    echo "Unit tests failed, skipping integration & e2e"
    FAILED=true
  fi
}

run_integration() {
  echo "=== Running Integration tests ==="
  if $COMPOSE run --rm -e TEST_TYPE=Integration app-test; then
    INTEGRATION_OK=true
  else
    echo "Integration tests failed, skipping e2e"
    FAILED=true
  fi
}

run_e2e() {
  echo "=== Running E2E tests ==="
  if $COMPOSE run --rm -e TEST_TYPE=E2E app-test; then
    E2E_OK=true
  else
    echo "E2E tests failed"
    FAILED=true
  fi
}

run_allure() {
  echo "=== Generating Allure report ==="
  $COMPOSE run --rm -e TEST_TYPE=Allure app-test || true
}

case "$TEST_TYPE" in
  b)
    run_build
    ;;
  u)
    run_unit
    ;;
  i)
    run_integration
    ;;
  e)
    run_e2e
    ;;
  a)
    run_allure
    ;;
  "" )
    run_build
	if [ "$FAILED" = false ]; then
		run_unit
	fi
    if [ "$FAILED" = false ]; then
      run_integration
    fi
    if [ "$FAILED" = false ]; then
      run_e2e
    fi
    run_allure
    ;;
  *)
    echo "Unknown test type: $TEST_TYPE"
    echo "Valid options: u=Unit, i=Integration, e=E2E, a=Allure"
    exit 1
    ;;
esac

echo "=== Summary ==="
echo "Build:       $BUILD_OK"
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
