#!/usr/bin/env bash
set -e

rm -rf dist/
rollup --config
cp src/*.html dist/
cp src/*.css dist/
cp src/kalah-worker.js dist/
cp src/favicon.ico dist/
cp src/webmanifest.json dist/
cp -R src/images/ dist/images/

