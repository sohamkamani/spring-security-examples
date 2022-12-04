# Spring Security Examples

This is the example repo for my blog post on [Spring Security Examples](java/spring-security)

There may be multiple examples in this project. Pick the example you want to run, and modify the `pom.xml` file to choose the main file. To do this, change the `mainClass` attribute in the Spring boot maven plugin.

For example, to run the example in [`src/main/java/com/sohamkamani/jwtauth`](src/main/java/com/sohamkamani/jwtauth), change the `pom.xml` configuration to:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.sohamkamani.jwtauth.Application</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

To run the application, execute the command:

```
 mvn clean compile package && java -jar ./target/spring-security-examples-0.0.1-SNAPSHOT.jar
```