saas.logging.demo
=================

Logging per request (without persistence)
-----------------------------------------

Logs are essential information for debugging an application. Logs comprises of two basic things,

1. logic (sequence of execution) and 
2. data (data which is under processing)

There are some instance where product support team lookup/search logs and try to determine the cause of failure of a request. 
Enabling logging per request, where logs are embedded in the response could be helpful for support. This article aims to address solution for such scenario.

Project
-------
The project consists of a hello world SOAP web service (JAX-WS based). In this project JAX-WS handlers and logback is utilized to achieve logging per request.

Refer: http://ourworkaround.blogspot.in/2013/12/logging-per-request-without-persistence.html

Build & Deploy
--------------
* Clone this project and do a maven build.
* Deploy generate WAR artifact (saas.logging.demo-1.0.war) in server (this was tested on JBoss AS)
* Test web service with soapUI (or any other tool).

