#!/usr/bin/env sh

CLUSTER=cafe-cluster
SERVICE=cafe-server
VERSION=$(git describe --tags)
AWS_PROFILE=dns ./gradlew :jib -Djib.to.tags=latest,"${VERSION}",test
AWS_PROFILE=dns aws ecs update-service --cluster ${CLUSTER} --service ${SERVICE} --force-new-deployment
