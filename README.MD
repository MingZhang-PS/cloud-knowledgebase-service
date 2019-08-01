# cloud-microservice-template

# INFO

#### Purpose
This is a microservice template for all java backend applications.

You can simply copy it to create new microservice.

#### Maintainers
- https://github.com/orgs/coresystemsFSM/teams/yoda

#### Pipeline
- https://bamboo.coresuite.com/browse/PIP-MT

#### Jira project
- [issue filter](https://jira.coresystems.net/browse/CPB-32197?jql=text%20~%20%22cloud-microservice-template%22)

#### Monitoring
- [Grafana](http://grafana.et-1.coreinfra.io/d/backend_cloud_microservices/backend-microservices?orgId=1&var-app=cloud-poc&var-pod=All) [docs](https://github.com/coresystemsFSM/documentation/wiki/Monitoring)
- [Kibana logs](https://elks-es.dev.coresuite.com/goto/32d87d5e45c257fc6dd587df86cbb01a)
- [Tracing](http://jaeger.et-1.coreinfra.io/search?end=1564063719071000&limit=20&lookback=1h&maxDuration&minDuration&service=cloud-poc&start=1564060119071000) [docs](https://github.com/coresystemsFSM/documentation/wiki/Logging-and-tracing)

#### Eventing
- topics
- events
- [docs](https://github.com/coresystemsFSM/documentation/wiki/Events-and-messaging)

# CONTRIBUTE

#### Dependencies
- database (see vault for credentials)
- vault [docs](https://github.com/coresystemsFSM/documentation/wiki/Vault)

#### How to run it locally
1. [Start database](https://github.com/coresystemsFSM/documentation/wiki/how-to--run-database)
2. Create intellij configuration (e.g. by copy-paste existing) and set profile `local`
3. Run app using intellij

#### Building
Use the following command: `mvn clean enforcer:enforce test -Pintegration package install -U`

It will clean, check for dependencies conflict and finally run integration tests.
