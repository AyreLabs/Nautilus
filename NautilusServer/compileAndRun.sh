#!/bin/sh

javac -cp ./Java-WebSocket-1.3.9.jar/ *.java
java -cp ./Java-WebSocket-1.3.9.jar/:. NautilusServer 8887 3000 terminal_inisitalization_string-0~0~0~0~0~0
