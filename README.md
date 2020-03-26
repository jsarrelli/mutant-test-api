# Mutan Test API

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

# Instructions

- For information regarding the endpoints we have OpenApi. See [Api-Docs](https://mutant-test-api.appspot.com/api-doc.html)
- url: https://mutant-test-api.appspot.com
- The API could be executed from the OpenApi documentation, from Postman or also from the tests that we are going to explain in a while.

# Testing
- Unit testing
    ```sh
    $ mvn verify
    ```
- Integration testing: to perform basic operations with standard expected results.
    ```sh
    $ mvn failsafe:integration-test
    ```
- Perfomance Testing: I include Gatling to perfom some Spike Tesing in order to see and to check the scalabily of the application.
    ```sh
    $ mvn gatling:test
    ```
    
# Infrastructure
The project is an Spring Boot Application hosted on Google Cloud Platform, running on an App Engine with Standard Environment. I choose this path because of the easy and rapid scalability that brings to the table.
The objective was to build an application that can handle great amount of RPS and I think this could an acceptable way of doing it. 
Altough a Flexible Environment that Google also allows is more easy to set up, the Standard ,on the other hand, is more efficient when it comes to scalability.
Also this application is integrated with a MySql database powered by Google Cloud, configurated with high availability so it can properly handle the requests from the several server intances that are running at the time.
The scaling is automatic and by demand, if the application need to it can increise it's instances and if none request are been made the instances will decrease up to 0.
