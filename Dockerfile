# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM gradle:7.0-jdk11 as builder

RUN mkdir /code
WORKDIR /code

ENV GRADLE_USER_HOME=/code/.gradlecache

COPY ./build.gradle.kts ./gradle.properties ./settings.gradle.kts /code/
COPY ./deprecated-javax/build.gradle.kts /code/deprecated-javax/

RUN gradle downloadDockerDependencies --no-watch-fs

COPY ./src/ /code/src

RUN gradle distTar --no-watch-fs \
    && cd build/distributions \
    && tar xzf *.tar.gz \
    && rm *.tar.gz radar-gateway-*/lib/radar-gateway-*.jar

FROM openjdk:11-jre-slim

MAINTAINER @blootsvoets

LABEL description="RADAR-base Gateway docker container"

RUN apt-get update && apt-get install -y \
  curl \
  && rm -rf /var/lib/apt/lists/*

COPY --from=builder /code/build/distributions/radar-gateway-*/bin/* /usr/bin/
COPY --from=builder /code/build/distributions/radar-gateway-*/lib/* /usr/lib/
COPY --from=builder /code/build/libs/radar-gateway-*.jar /usr/lib/

USER 101

EXPOSE 8090

CMD ["radar-gateway"]
