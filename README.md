# Java Blockchain Implementation

This is a simple implementation of a Blockchain as a Spring-boot application. It is not perfect. The aim of this project was to learn about the core concepts behind a typical blockchain. Feedback is more than welcome!

# Requirements

you wil need:
* Java 8 or later
* Maven 
* An HTTP Client, such as [Postman](https://www.getpostman.com/) 

# Setup

* Clone the repository into a known folder
* Open your command prompt to that folder location
* run `mvn clean install`
* open two other command prompts in the same location (just write `start` if on Windows)
* run `mvn spring-boot:run` in each command prompt
* take note of the generated port in each console as that will be needed for registering nodes.

# Testing APIs

A quick way to test the APIs is to use the following Postman [collection](https://www.getpostman.com/collections/1f4bcb9133053b22ce49) in the given order. Make sure to replace the port variables with the correct port values, or create a Postman Environment to quickly and conveniently fill in port values.


