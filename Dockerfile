FROM registry.dev.coresuite.com/docker-base-java:0.3.0

ARG application_name
ARG application_version

ENV JVM_EXTRA_ARGS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/oom_dump_file"

COPY "target/$application_name-$application_version.jar"  app.jar

EXPOSE 8080