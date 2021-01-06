#!/bin/bash
[ -z "${_source_mark_of_common:+dummy}" ] || return 0
_source_mark_of_common=true

set -eEuo pipefail

################################################################################
# constants
################################################################################

# NOTE: $'foo' is the escape sequence syntax of bash
readonly nl=$'\n'        # new line
readonly ec=$'\033'      # escape char
readonly eend=$'\033[0m' # escape end

################################################################################
# common util functions
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
}

# How to compare a program's version in a shell script?
#   https://unix.stackexchange.com/questions/285924
versionLessThan() {
    (($# == 2)) || die "versionLessThan must 2 arguments"
    local ver=$1
    local destVer=$2

    [ "$ver" = "$destVer" ] && return 0

    [ "$(printf '%s\n' "$ver" "$destVer" | sort -V | head -n1)" = "$ver" ]
}

logAndRun() {
    local simple_mode=false
    [ "$1" = "-s" ] && {
        simple_mode=true
        shift
    }

    if $simple_mode; then
        echo "Run under work directory $PWD : $*"
        "$@"
    else
        blueEcho "Run under work directory $PWD :$nl$*"
        time "$@"
    fi
}

die() {
    redEcho "Error: $*" 1>&2
    exit 1
}
