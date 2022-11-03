# `useful-articles` - Article management system

This system is intended to be a knowledge base for teams. It uses the [Markdown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet) to write the articles.

The articles are organized in hierarchical structures managed by labels.
Every label can has parent labels and you basically has the tree where the labels are nodes and your articles are leafs.

And of course, all info is indexed and can be searched.

# Architecture

The project consists from server-side backed written with Java and [Spring Boot](https://spring.io/projects/spring-boot) and [Spring Cloud](https://spring.io/projects/spring-cloud). It uses asynchronous approach based on [WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html).

The client side is based on [Angular](https://angular.io/) and [PrimeNg](https://www.primefaces.org/primeng/)

The Database is [PostgreSQL](https://www.postgresql.org/) but could be easily replaced with any other one supported by [Spring Data](https://spring.io/projects/spring-data) 
and (Spring Data R2DBC)[https://spring.io/projects/spring-data-r2dbc]

For authentication we are using OAuth2 and you could use some of the well known services like `GMail` or `Facebook` to authenticate.