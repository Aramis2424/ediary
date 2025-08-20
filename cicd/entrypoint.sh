#!/bin/bash
set -e

echo ">>> Run tests: $TEST_TYPE"

case $TEST_TYPE in
  Unit)
    ./gradlew uTest --no-daemon
    ;;
  Integration)
    ./gradlew iTest --no-daemon
    ;;
  E2E)
    ./gradlew eTest --no-daemon
    ;;
  Allure)
    ./gradlew alreport --no-daemon
    ;;
  *)
    echo "Unknown TEST_TYPE: $TEST_TYPE"
    exit 1
    ;;
esac
