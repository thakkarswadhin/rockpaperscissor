# rockpaperscissor
Online Rock Paper Scissors Game between a client and a server

DOCUMENTATION: ONLINE ROCK-PAPER-SCISSORS GAME

Usage Instructions:
By default, the server will run on local host. Therefore, the default address for client is “localhost:port/play”.
Open two command prompts, type following commands:
1.	javac MyServer.java
java MyServer
<number of rounds to be played>
<unused port number on server>
2.	javac MyServer.java
java MyClient
localhost
<enter same port number as server>
Follow on the on-screen instructions to play the game. 

References:
1.	CMU course 08-722 class notes
2.	https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
3.	http://www.rgagnon.com/javadetails/java-have-a-simple-http-server.html
4.	https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html
5.	https://buddhimawijeweera.wordpress.com/2014/12/25/creating-http-server-with-java/

To-Do (for running on Eclipse IDE):
1.	To use Sun’s packages for HTTP, you need to disable error prompts in Eclipse:
Project properties -> Java Compiler -> Errors/Warnings -> Deprecated and restricted API 
https://stackoverflow.com/questions/9579970/can-not-use-the-com-sun-net-httpserver-httpserver

Time Taken:
1.	~7 hours

Design Decisions / Features:
1.	Server-intensive model – All the error handling and computation is taking place at the server. This makes the client-side processing and light
2.	Use of Enum – Rock, Paper, Scissor are enums and not just strings
3.	All the code follows the coding convention
4.	Use of Java 8
5.	Rock, Paper, Scissor input string is ignorant of case to handle variation in input.
6.	Client can send both HTTP GET and POST requests
7.	Server supports both HTTP GET and POST requests
8.	Best-of-n (please note – the client must reconnect for each round)
9.	My own extension – A secure online Rock-Paper-Scissor game
10.	Use of SHA-256 encryption to send secure messages over the internet

Limitations / Possible improvements:
1.	Used many string concatenations. Can use String Builder class in Java instead.
2.	All methods need not be static. Can instantiate the object before calling methods.
