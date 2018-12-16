#!/bin/sh

./screen -S "Nautilus$1" -X hardcopy -h "../_temp_screendump_Nautilus$1";cat "_temp_screendump_Nautilus$1" 

