#!/usr/bin/env bash
docker run --name cloud-knowledgebase-service -e POSTGRES_PASSWORD=cloud-knowledgebase-service -e POSTGRES_USER=cloud-knowledgebase-service -e POSTGRES_DB=cloud-knowledgebase-service -p 5432:5432 -d postgres:9.6