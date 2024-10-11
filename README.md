<div align="center">

  # Telegram Bot

  <img width="250px" style="border-radius:50%" alt="bot logo" src="./logo.jpg"/>

</div>

## ✨ About 
This project is a learning-focused Telegram bot that I developed to explore various technologies, architectures, and tools.

Currently, the bot allows users, who have confirmed their email address, to send text commands as well as images and documents.
The files are fetched from Telegram's servers via the Telegram Bot API and are stored in a local database, returning download links to users.

My focus is now on upcoming projects, but if I come across an interesting and affordable😁 service for processing images or documents, I might enhance the bot to include such features instead of returning just a simple downloading link.

## 🎯 Main goals
The most important aspect of the project was to implement and study the `technologies listed below`, and of course `to solve many unexpected cases` that occurred during the development process:

* `Microservices architecture`
* `Docker `
  - **configuration** for microservices 
  - **healthchecks** + **depends_on** to create some order of containers start-up
  - **volumes** and **bind mounts** to save data and share folders
  - used convenient **YAML variables** in docker-compose file
* `RabbitMQ`
  - message broker for **asynchronous communication between microservices**
  - I used the approach of copying **the prepared configuration file 'definitions.json'** to the docker container, to have a **ready-made RabbitMQ Server** right after creating a rabbitmq container (for example, to specify needed users)
* `Telegram Bot API`
  - I decided **to download files from the Telegram storage in chunks using partial requests** (despite the fact that the maximum uploading file size in this API is 20Mb)
* `Spring Boot`
* `Java Mail Sender libraries`
* `Thymeleaf`
  - for cool **markup templates in email-activation letters**
* `RestTemplate`
  - **Spring synchronous http-client library**
* `Liquibase`
  - **database change management**
* `Spring Data JPA`
  - implementing a **data access layer**, handling **transactions**

## 🚀 Areas for improvement
* Avoid usage of `a single database for multiple microservices` 
  - Challenging and complex decision due to the principles of microservices architecture. Typically, microservices are designed to be autonomous, each owning its own data to maintain loose coupling and independent scalability


* Avoid usage of `Liquibase as a separate docker container`
  - Better to attach liquibase to microservices that works with db to execute migrations automatically when microservice starts, and you will immediately see all unappropriated db changes. In my case it remains as a separate container to make a single place for liquibase changesets, and also I had some issues specifying path to the liquibase 'changelog' file that located out of microservices classpath


* Reconsider the selection of `the appropriate message broker`
  - Apache Kafka is better suited for projects like this, as one of its main advantages is working with large volumes of data. RabbitMQ is ideal for blocking tasks (thanks to its 'Priority' feature) and allows for faster server response time


* Instead of just copying the jar file, I did some `Dockerfile enhancement` 
  - Implement `multistaging`: `build stage` (just creating jar with Maven); `jar_extract stage` (Spring Boot layertools approach); `app running stage` (run the app using JRE image)
  - Thanks to the previous point with build stage - we can make `the project build the same for all developers of project` and automate this step
  - We can use `layertools` (Spring Boot app launch mode) with it's `extract` command - to divide jar into separate logical folders (e.g. source code, dependencies, resources...) and individually `COPY` them to docker container, enabling better use of `layer caching` functionality in image building process
  - Also in Dockerfile we handle `issues with parent and dependent pom files` at build stage   

## ⚡ How to run 
  1. Ensure you have installed `Docker Desktop` and `Git`
  2. Clone remote git repository to your local machine:
  ```console
  git clone https://github.com/denys-mg/telegram-bot.git
  ```
  3. Set up `.env` file:
     - You need to configure `Telegram Bot` and `Mail Sender`, all the rest configurations is optional!
     - `Telegram Bot` - go to [BotFather][botfather_url] and create bot following simple instructions. As a result, you should get `bot token` and `bot username` values, which you need to write in the `.env` configuration
     - `Mail Sender` - prepare `a Gmail account` to send email activation letters. For this you need to generate `application password` -> go to Gmail -> Manage your Google account -> Security -> Enable two-step verification. After enter `sign in with app passwords` in the account settings search bar, and generate this `16-digit code` (the requested name of the app doesn't matter)
     - You can modify all other settings, but if you decide to change `the user's name and password in RabbitMQ` - then you will also need to additionally change the `/common-rabbitmq/definitions.json` file (this config file can be generated by the export function of the RabbitMQ management web interface)
  4. Run the application:
  ```console
  docker compose up
  ```
  - All logs you can see in Docker containers file system in `/app/logs/`  

## 🛠️ Used tools
+ JDK 17
+ Docker 23.0.5
+ RabbitMQ 3.13.2
+ Telegram Bot API 6.1.0
+ Spring Boot 2.5.2
+ Spring Data JPA 2.5.2
+ Spring Web 5.3.8
+ PostgreSQL 14.5
+ Liquibase 4.29.1
+ Jakarta Mail 1.6.7
+ Thymeleaf 3.0.15
+ Apache Maven 3.8.7
+ Lombok 1.18.22
+ Log4j 1.2.17
+ Checkstyle maven plugin 3.1.1

## 👀 Project overview
![Project Overview](./ProjectOverview.gif)

<!-- Readme links -->
[botfather_url]: https://t.me/BotFather
