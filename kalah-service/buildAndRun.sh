#!/usr/bin/env bash
set -e

mvn clean package
mvn spring-boot:run