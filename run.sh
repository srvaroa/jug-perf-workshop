#!/bin/bash
#
# Exercise runner.
#
set -e
function usage {
    echo "usage: ./run.sh <exercise_number> <exercise_options>"
}

function execute {
    java -server \
        $jvm_profiling \
        $jvm_collector \
        $jvm_memory \
        $jvm_gc_logs \
        $jvm_print_safepoint_stats \
        $jvm_print_tlab \
        $jvm_jit_logs \
        -cp build/libs/*-all.jar \
        com.awesome.ex$ex_number.Ex$ex_number "$@"
}

if [ "$#" -lt "1" ]
then
    usage
    exit 1
fi

./gradlew shadowJar
ex_number=$1
shift

########################
### EDIT BELOW THIS LINE
########################

gc_log_file="./build/Ex_${ex_number}_gc_$(date +%s).log"
jvm_profiling="-XX:+UnlockCommercialFeatures -XX:+FlightRecorder"
jvm_collector="-XX:+UseG1GC"
jvm_memory="-Xmx256m -Xms256m"
jvm_gc_logs="-XX:+PrintGCDetails -XX:+PrintGCDateStamps"
jvm_gc_logs="$jvm_gc_logs -Xloggc:$gc_log_file"
jvm_gc_logs="$jvm_gc_logs -XX:+PrintGCApplicationStoppedTime"
#jvm_gc_logs="$jvm_gc_logs -XX:+PrintAdaptiveSizePolicy"
#jvm_gc_logs="$jvm_gc_logs -XX:+PrintTenuringDistribution"
#jvm_print_safepoint_stats="-XX:+PrintSafepointStatistics"
#jvm_print_tlab="-XX:+PrintTLAB"
#jvm_jit_logs="-XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation"

execute $@

echo GC logs at $gc_log_file
