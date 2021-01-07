#!/bin/bash
set -eEuo pipefail
# adjust current dir to script dir
cd "$(dirname "$(readlink -f "$0")")"

source common.sh
source prepare-jdk.sh

################################################################################
# multi JDK CI operations
################################################################################

# shellcheck disable=SC2154
for jhm_var_name in "${java_home_var_names[@]}"; do
    export JAVA_HOME="${!jhm_var_name}"

    echo
    headInfo "test with $jhm_var_name: $JAVA_HOME"
    echo

    logAndRun ./integration-test.sh
done
