# Spring Security Examples

This is the example repo for my blog posts on [Spring Security](https://www.sohamkamani.com/categories/spring-security/)

There may be multiple examples in this project. Pick the example you want to run, and set the `-DMAIN_CLASS` flag when running Maven.

For example, to run the example in [`src/main/java/com/sohamkamani/jwtauth`](src/main/java/com/sohamkamani/jwtauth), you should execute this command:


```
mvn -DMAIN_CLASS=com.sohamkamani.jwtauth.Application clean compile package && java -jar ./target/spring-security-examples-0.0.1-SNAPSHOT.jar
```