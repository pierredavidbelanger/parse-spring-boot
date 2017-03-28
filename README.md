# parse-spring-boot

Parse Spring Boot is a collection of starter libraries to help integrating [Parse Server](http://parseplatform.org/) backend features into a Spring Boot application that will act as a client using [ParseClient](https://github.com/pierredavidbelanger/parseclient).

See [parse-spring-boot-starter-example](parse-spring-boot-starter-example) for a working example application integrating [parse-spring-boot-starter](parse-spring-boot-starter) and [parse-security-spring-boot-starter](parse-security-spring-boot-starter).

## parse-spring-boot-starter

A Starter to integrate [ParseClient](https://github.com/pierredavidbelanger/parseclient) into a Spring Boot application.

Just include this dependency into your Spring Boot application (see [example](parse-spring-boot-starter-example/pom.xml)).

```xml
<dependency>
    <groupId>ca.pjer</groupId>
    <artifactId>parse-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

Then, define some properties (into your `application.properties` (see [example](parse-spring-boot-starter-example/application.properties)):

```properties
parse.serverUri=http://localhost:1337/parse # this is the default value
parse.applicationId=MyAppId
parse.restApiKey=MyRestApiKey
parse.masterKey=MyMasterKey
```

Then, inject some beans (see [example](parse-spring-boot-starter-example/src/main/java/ca/pjer/spring/boot/parse/example/Main.java)):

```java
@Autowired
ca.pjer.parseclient.ParseClient parseClient;

// Injected only if `parse.applicationId` is defined
@Autowired(required = false)
ca.pjer.parseclient.Application application;

@Autowired(required = false)
@Qualifier("anonymousPerspective")
ca.pjer.parseclient.Perspective anonymousPerspective;

// Injected only if `parse.masterKey` is defined
@Autowired(required = false)
@Qualifier("masterPerspective")
ca.pjer.parseclient.Perspective masterPerspective;
```

## parse-security-spring-boot-starter

A Starter to add Spring Security based on Parse users/session into a Spring Boot Web application.

Just include this dependency into your Spring Boot Web application (see [example](parse-spring-boot-starter-example/pom.xml)).

```xml
<dependency>
    <groupId>ca.pjer</groupId>
    <artifactId>parse-security-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

A couple of properties are available for customisation, but out of the box you get authentication and authorization into your app with zero config.

All URIs will requires authentication.

A login page is available at `/login`.

Also, you can inject a `ParseSession` into your Spring Web controllers methods (see [example](parse-spring-boot-starter-example/src/main/java/ca/pjer/spring/boot/parse/example/Main.java)):

```java
@RestController
public static class MainController {

    @GetMapping("/me")
    public Object me(ParseSession parseSession) {
        return parseSession;
    }
}
```