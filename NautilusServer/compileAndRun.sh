#!/bin/sh

javac -cp ./Java-WebSocket-1.3.9.jar/ *.java
#java -cp ./Java-WebSocket-1.3.9.jar/:. NautilusServer 8887 3000 terminal_inisitalization_string~-7~0.75~-5.5~0~0~0~15 terminal_inisitalization_string~-2.5~0.75~9~0~115~0~15 terminal_inisitalization_string~-2.5~0.75~9~0~115~0~15
java -cp ./Java-WebSocket-1.3.9.jar/:. NautilusServer 8887 3000 terminal_inisitalization_string~-4.5~0.75~-5.5~0~0~0~15 terminal_inisitalization_string~-2.5~0.75~7.5~0~115~0~15 terminal_inisitalization_string~6.5~0.75~-2.5~0~250~0~15
