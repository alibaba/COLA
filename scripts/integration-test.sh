#!/bin/bash
set -eEuo pipefail

################################################################################
# constants
################################################################################

# NOTE: $'foo' is the escape sequence syntax of bash
readonly nl=$'\n' # new line
readonly ec=$'\033' # escape char
readonly eend=$'\033[0m' # escape end


################################################################################
# util functions
################################################################################

colorEcho() {
    local color=$1
    shift

    # if stdout is the console, turn on color output.
    [ -t 1 ] && echo "${ec}[1;${color}m$*${eend}" || echo "$*"
}

redEcho() {
    colorEcho 31 "$@"
}

yellowEcho() {
    colorEcho 33 "$@"
}

blueEcho() {
    colorEcho 36 "$@"
}

headInfo() {
    colorEcho "0;34;46" ================================================================================
    yellowEcho "$*"
    colorEcho "0;34;46" ================================================================================
    echo
}

runCmd() {
    blueEcho "Run under work directory $PWD :$nl$*"
    time "$@"
}

die() {
    redEcho "Error: $*" 1>&2
    exit 1
}


################################################################################
# CI operations
################################################################################

readonly -a MVN_CMD=(
    ./mvnw -V --no-transfer-progress
)

(
    headInfo "CI: cola-components"

    cd cola-components/
    runCmd "${MVN_CMD[@]}" install
)

(
    headInfo "CI: cola-archetypes"

    cd cola-archetypes/
    runCmd "${MVN_CMD[@]}" install
)

(
    headInfo "CI: sample/craftsman"

    cd sample/craftsman/
    runCmd "${MVN_CMD[@]}" install
)
