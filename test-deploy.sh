#!/usr/bin/env sh

CLUSTER=cafe
SERVICE=cafe-server
VERSION=$(git describe --tags)
AWS_PROFILE=cafe ./gradlew :jib -Djib.to.tags=latest,"${VERSION}",test
AWS_PROFILE=cafe aws ecs update-service --cluster ${CLUSTER} --service ${SERVICE} --force-new-deployment
