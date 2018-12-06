#!/bin/sh

javac -cp ./java-websocket-1.3.9.jar *.java
java -cp ./Java-WebSocket-1.3.9.jar:. NautilusKeyboardClient "ws://127.0.0.1:8887"
