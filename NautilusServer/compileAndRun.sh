#!/bin/sh

javac -cp ./Java-WebSocket-1.3.9.jar/ *.java
java -cp ./Java-WebSocket-1.3.9.jar/:. ChatServer
