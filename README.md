# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#production-ready)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Quartz](http://www.quartz-scheduler.org/downloads/)
* [Docker-Mysql](https://hub.docker.com/_/mysql)
* [Docker-Desktop](https://www.docker.com/products/docker-desktop)
* [Docker-Desktop](https://www.docker.com/products/docker-desktop)

## Technology stack
* Java 11
* Spring Boot v2.2.1.RELEASE
* Docker 19.03.5
* MySql 8.0
* Quartz 2.3.2
* Hibernate 5.4.8.Final
* Flyway 6.0.8

### How to add new job
Create new Job in jobs folder and extends from QuartzJobBean

### Before Running the Application

* Install Docker UI with corresponding configurations
* Run MySQL docker container ```docker run -p 3306:3306 --name quartz -e MYSQL_ROOT_PASSWORD=mypassword -e MYSQL_DATABASE=quartz -d mysql```

#### Or
* Install MySql server locally

## Run application:
mvn spring-boot:run

## Job Management Endpoints

### Schedule Job:

* Endpoint:  http://localhost:8080/api/scheduler/schedule
* Request Type: Post
* Request Body - Scheduler with cron job

```
{
  "jobName": "EmailJob",
  "jobGroup": "EmailGroup",
  "description": "Email job",
  "priority": 10,
  "jobClass" : "com.hhovhann.jobmanagement.jobs.email.EmailJob",
  "cronJob":true,
  "cronExpression": "0 */1 * * * ?"
}
```

* Request Body - Scheduler with immediately job

```
{
  "jobName": "StoreContentJob",
  "jobGroup": "StoreContentGroup",
  "description": "Store Content job",
  "priority": 15,
  "jobClass" : "com.hhovhann.jobmanagement.jobs.email.EmailJob",
  "cronJob": false
}
```

### UnSchedule Job
* Endpoint:  http://localhost:8080/api/scheduler/unschedule//{jobName}/{jobGroup}
* Example:   http://localhost:8080/api/scheduler/unschedule/EmailJob/EmailGroup
* Request Type: Post

### Pause Job
* Endpoint:  http://localhost:8080/api/scheduler/pause/{jobName}/{jobGroup}
* Example:   http://localhost:8080/api/scheduler/pause/EmailJob/EmailGroup
* Request Type: Post

### Resume Job
* Endpoint:  http://localhost:8080/api/scheduler/resume/{jobName}/{jobGroup}
* Example:   http://localhost:8080/api/scheduler/resume/EmailJob/EmailGroup
* Request Type: Post

### Delete Job
* Endpoint:  http://localhost:8080/api/scheduler/delete/{jobName}/{jobGroup}
* Example:   http://localhost:8080/api/scheduler/delete/EmailJob/EmailGroup
* Request Type: Post

## Testing
### Run all the unit test classes.
mvn test

*NOTE:* Before running all tests be sure that all tables in quartz db are dropped