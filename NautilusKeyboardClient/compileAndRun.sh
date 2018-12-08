#!/bin/sh

#------------------------------------------------------------------------------------------
#
# PURPOSE
# -------
# ...
#
# AUTHOR
# ------
# Ayre Labs (2018)
#
#------------------------------------------------------------------------------------------

#------------------------------------------------------------------------------------------
# MAIN
#------------------------------------------------------------------------------------------
main()
{
    echo "\n>> Compiling..."
    compileNautilusKeyboardClient
    echo ">> done\n"
    
    echo "\n>> Running..."
    runNautilusKeyboardClient
    echo ">> done\n"
}

#------------------------------------------------------------------------------------------
# INTERNAL FUNCTIONS
#------------------------------------------------------------------------------------------
function compileNautilusKeyboardClient()
{
    javac -cp ./java-websocket-1.3.9.jar *.java
}

function runNautilusKeyboardClient()
{
    java -cp ./Java-WebSocket-1.3.9.jar:. NautilusKeyboardClientApplication "ws://127.0.0.1:8887"
}

#------------------------------------------------------------------------------------------
# SCRIPT
#------------------------------------------------------------------------------------------
    main
