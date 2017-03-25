# parse-spring-boot-starter

A Starter to integrate [ParseClient](https://github.com/pierredavidbelanger/parseclient) into a Spring Boot application.

## Usage

See [parse-spring-boot-starter-example](parse-spring-boot-starter-example) for a working example application.

Just include this dependency into your Spring Boot application (see [example](parse-spring-boot-starter-example/pom.xml)).

```xml
<dependency>
    <groupId>ca.pjer</groupId>
    <artifactId>parse-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then, define some properties (into you `application.properties` (see [example](parse-spring-boot-starter-example/application.properties)):

```properties
parse.serverUri=http://localhost:1337/parse # this is the default value
parse.applicationId=MyAppId
parse.restApiKey=MyRestApiKey
parse.masterKey=MyMasterKey
```

Then, inject some beans (see [example](parse-spring-boot-starter-example/src/main/java/ca/pjer/spring/boot/parse/example/Main.java)):

```java
@Autowired(required = false)
ca.pjer.parseclient.ParseClient parseClient;

// Injected only if `parse.applicationId is defined`
@Autowired(required = false)
ca.pjer.parseclient.Application application;

@Autowired(required = false)
@Qualifier("anonymousPerspective")
ca.pjer.parseclient.Perspective anonymousPerspective;

// Injected only if `parse.masterKey is defined`
@Autowired(required = false)
@Qualifier("masterPerspective")
ca.pjer.parseclient.Perspective masterPerspective;
```