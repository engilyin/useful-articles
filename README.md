# `useful-articles` - Article management system

This system is intended to be a knowledge base for teams. It uses the [Markdown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet) to write the articles.

The articles are organized in hierarchical structures managed by labels.
Every label can has parent labels and you basically has the tree where the labels are nodes and your articles are leafs.

And of course, all info is indexed and can be searched.

# The project backlog and issue tracker

You can track the progress, tell about the bug or request the new feature at the [Project Backlog](https://www.pivotaltracker.com/n/projects/2611059)

# Architecture

Please check the project [Inception Desk presentation](docs/Useful-Articles_Inception-Desk.odp) for better understanding what is it.

The project consists from server-side backed written with Java and [Spring Boot](https://spring.io/projects/spring-boot) and [Spring Cloud](https://spring.io/projects/spring-cloud). It uses asynchronous approach based on [WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html).

The client side is based on [Angular](https://angular.io/)

The Database is [PostgreSQL](https://www.postgresql.org/) but could be easily replaced with any other one supported by [Spring Data](https://spring.io/projects/spring-data) 
and (Spring Data R2DBC)[https://spring.io/projects/spring-data-r2dbc]

For authentication we are using OAuth2 and you could use some of the well known services like `GMail` or `Facebook` to authenticate.

# Build and run


to build project enter the root project directory and run:
```
./gradlew clean build
```

to build docker image run:
```
docker build -t useful-articles .
```

to run the app make sure you have up and running the `PostgreSQL` database and supply the credentials for it as environment variables.

```
 docker run -d -p 8080:8080 --name myarticles useful-articles ....supply other params
 ```
 
 or just start it with the script and docker-compose:
 ```
export EXTERNAL_IP=<your machine IP address>; docker-compose up
 ```