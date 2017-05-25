#!/bin/bash

function ok {
    echo -e "\033[32m$@ ok\033[0m"
}

function not_ok {
    echo -e "\033[31m$@ failed!\033[0m"
}

function evaluate {
    ret="$1"
    shift
    if [ "$ret" == "0" ]
    then
        ok $@
    else
        not_ok $@
    fi
}

tools="java javac jstat jvisualvm jstack jmap"
for t in $tools
do
    which $t &> /dev/null
    evaluate $? `basename $t`
done

java -version 2>&1 | grep HotSpot 2>&1 /dev/null | test -n
evaluate $? "Java is HotSpot"

javac -version 2>&1 | grep HotSpot 2>&1 /dev/null | test -n
evaluate $? "Java compiler is HotSpot"

java -version 2>&1 | grep "1.8" 2>&1 /dev/null | test -n
evaluate $? "Java is 1.8"

java -version 2>&1 | grep "1.8" 2>&1 /dev/null | test -n
evaluate $? "Java compiler is 1.8"
