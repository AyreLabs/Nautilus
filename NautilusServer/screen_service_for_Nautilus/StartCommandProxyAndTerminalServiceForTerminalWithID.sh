#!/bin/bash

./screen -S "NautilusCommandProxy$1" -d -m ./SystemCommandProxy `expr 6789 + $1`
./SSfN_ManuallyStartTerminalWithID.sh $1

