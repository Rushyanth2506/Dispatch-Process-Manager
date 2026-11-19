#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
command -v javac >/dev/null || { echo "Install JDK 17+ with javac."; exit 1; }
javac -d out src/*.java
java -cp out Main
