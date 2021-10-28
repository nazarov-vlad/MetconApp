#!/bin/bash
mydate=$(date '+%d-%m-%Y_%H-%M-%S')
git commit -a -m $mydate
git push -u MetconApp master
