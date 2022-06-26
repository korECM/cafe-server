# Getting Started

[![codecov](https://codecov.io/gh/korECM/cafe-server/branch/main/graph/badge.svg?token=79UW7T4L1W)](https://codecov.io/gh/korECM/cafe-server)

## Build
```shell
VERSION=$(git describe --tags)
AWS_PROFILE=cafe ./gradlew :jib -Djib.to.tags=latest,${VERSION}
```


```shell
VERSION=$(git describe --tags) # ex v1.0.0
LOCAL_NAME=cafe
ECR_REPO=183624387110.dkr.ecr.ap-northeast-2.amazonaws.com/cafe


docker image prune -a
./gradlew clean 
./gradlew build 
docker buildx build --platform=linux/amd64 --build-arg JAR_FILE=build/libs/\*.jar -t ${LOCAL_NAME}:latest .
docker tag ${LOCAL_NAME}:latest ${LOCAL_NAME}:${VERSION}
docker tag ${LOCAL_NAME}:latest ${ECR_REPO}:latest
docker tag ${LOCAL_NAME}:latest ${ECR_REPO}:${VERSION}

# test 버전 올리고싶다면
docker tag ${LOCAL_NAME}:latest ${ECR_REPO}:test

aws ecr --profile cafe get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 183624387110.dkr.ecr.ap-northeast-2.amazonaws.com
docker push ${ECR_REPO} --all-tags
```

### 한 줄 버전

```shell
VERSION=$(git describe --tags) # ex v1.0.0
LOCAL_NAME=cafe
ECR_REPO=183624387110.dkr.ecr.ap-northeast-2.amazonaws.com/cafe
aws ecr --profile cafe get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 183624387110.dkr.ecr.ap-northeast-2.amazonaws.com

docker image prune -a && ./gradlew clean && ./gradlew build && docker buildx build --platform=linux/amd64 --build-arg JAR_FILE=build/libs/\*.jar -t ${LOCAL_NAME}:latest . && docker tag ${LOCAL_NAME}:latest ${LOCAL_NAME}:${VERSION} && docker tag ${LOCAL_NAME}:latest ${ECR_REPO}:latest && docker tag ${LOCAL_NAME}:latest ${ECR_REPO}:${VERSION} 

# test 버전 올리고싶다면
docker tag ${LOCAL_NAME}:latest ${ECR_REPO}:test

docker push ${ECR_REPO} --all-tags
```
