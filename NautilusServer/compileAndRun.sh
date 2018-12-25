#!/bin/bash

#------------------------------------------------------------------------------------------
#
# PURPOSE
# -------
# Compile java code for the Nautilus Server and run the Server Application
#
# AUTHOR
# ------
# Ayre Labs (2018)
#
#------------------------------------------------------------------------------------------

#------------------------------------------------------------------------------------------
# DEFINITIONS
#------------------------------------------------------------------------------------------
readonly JAVA_WEBSOCKET_LIBRARY=./Java-WebSocket-1.3.9.jar/
readonly CLIENT_KEYBOARD_SOCKET="8887"
readonly CLIENT_VR_SOCKET="3000"
readonly TERMINAL_ONE_INITIALIZATION_STRING="terminal_inisitalization_string~-4.5~0.75~-5.5~0~0~0~15"
readonly TERMINAL_TWO_INITIALIZATION_STRING="terminal_inisitalization_string~-2.5~0.75~7.5~0~115~0~15"
readonly TERMINAL_THREE_INITIALIZATION_STRING="terminal_inisitalization_string~6.5~0.75~-2.5~0~250~0~15"

#------------------------------------------------------------------------------------------
# MAIN
#------------------------------------------------------------------------------------------
main()
{
    echo "Compiling..."
    compileNautilusServer
    echo "Done."
    
    echo "Running..."
    runNautilusServer
    echo "Done."
}

#------------------------------------------------------------------------------------------
# INTERNAL FUNCTIONS
#------------------------------------------------------------------------------------------
function compileNautilusServer()
{
	javac -cp $JAVA_WEBSOCKET_LIBRARY *.java
}

function runNautilusServer()
{
	java -cp $JAVA_WEBSOCKET_LIBRARY:. NautilusServer $CLIENT_KEYBOARD_SOCKET $CLIENT_VR_SOCKET $TERMINAL_ONE_INITIALIZATION_STRING $TERMINAL_TWO_INITIALIZATION_STRING $TERMINAL_THREE_INITIALIZATION_STRING
}

#------------------------------------------------------------------------------------------
# SCRIPT
#------------------------------------------------------------------------------------------
    main
