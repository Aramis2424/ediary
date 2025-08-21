#!/usr/bin/env bash
set -e

COMPOSE="docker compose -f ./docker-compose.test.yml -p ediary_cicd"

UNIT_OK=false
INTEGRATION_OK=false
E2E_OK=false
FAILED=false

generate_skipped() {
  local TEST_NAME=$1
  local REASON=$2
  local UUID_RESULT=$(uuidgen)
  local UUID_CONTAINER=$(uuidgen)
  local NOW=$(date +%s%3N)

  cat > ./allure-results/${UUID_RESULT}-result.json <<EOF
{
  "uuid": "${UUID_RESULT}",
  "historyId": "${TEST_NAME// /_}_history",
  "testCaseId": "${TEST_NAME}.fakeSkippedTest",
  "testCaseName": "fakeSkippedTest",
  "fullName": "${TEST_NAME}.fakeSkippedTest",
  "labels": [
    {"name": "package", "value": "org.fake"},
    {"name": "testClass", "value": "${TEST_NAME}Class"},
    {"name": "testMethod", "value": "fakeSkippedTest"},
    {"name": "suite", "value": "${TEST_NAME}"}
  ],
  "name": "${TEST_NAME}",
  "status": "skipped",
  "statusDetails": {
    "message": "${REASON}"
  },
  "stage": "finished",
  "start": ${NOW},
  "stop": ${NOW}
}
EOF

  cat > ./allure-results/${UUID_CONTAINER}-container.json <<EOF
{
  "uuid": "${UUID_CONTAINER}",
  "children": ["${UUID_RESULT}"],
  "befores": [],
  "afters": [],
  "start": ${NOW},
  "stop": ${NOW}
}
EOF
}

echo "=== Running Unit tests ==="
if $COMPOSE run --rm -e TEST_TYPE=Unit app-test; then
  UNIT_OK=true
else
  echo "Unit tests failed, skipping integration & e2e"
  generate_skipped "Integration tests" "Skipped because Unit tests failed"
  generate_skipped "E2E tests" "Skipped because Unit tests failed"
  FAILED=true
fi

if [ "$FAILED" = false ]; then
  echo "=== Running Integration tests ==="
  if $COMPOSE run --rm -e TEST_TYPE=Integration app-test; then
    INTEGRATION_OK=true
  else
    echo "Integration tests failed, skipping e2e"
	generate_skipped "E2E tests" "Skipped because Integration tests failed"
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
