#!/bin/bash
# NOTE about Bash Traps and Pitfalls:
#
# 1. DO NOT declare var as readonly if value is supplied by subshell!!
#    for example: readonly var1=$(echo value1)
#
#    readonly declaration make exit code of assignment to be always 0,
#      aka. the exit code of command in subshell is discarded.
#      tested on bash 3.2.57/4.2.46

set -eEuo pipefail

# adjust current dir to project root dir
cd "$(dirname "$0")/.."

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

################################################################################
# build util functions
################################################################################

readonly -a MVN_OPTIONS=(
    -V --no-transfer-progress
)

MVN() {
    local maven_wrapper_name="mvnw"

    local d="$PWD"
    while true; do
        [ "/" = "$d" ] && die "Fail to find $maven_wrapper_name!"
        [ -f "$d/$maven_wrapper_name" ] && break

        d=$(dirname "$d")
    done

    logAndRun "$d/$maven_wrapper_name" "${MVN_OPTIONS[@]}" "$@"
}

# Where is Maven local repository?
#   https://mkyong.com/maven/where-is-maven-local-repository/

# NOTE: DO NOT declare mvn_local_repository_dir var as readonly, its value is supplied by subshell.
mvn_local_repository_dir="$(
    cd cola-components/ &&
        ./mvnw --no-transfer-progress help:evaluate -Dexpression=settings.localRepository |
        grep '^/'
)"
[ -n "$mvn_local_repository_dir" ] || die "Fail to find find maven local repository directory: $mvn_local_repository_dir"

echo "find maven local repository directory: $mvn_local_repository_dir"

################################################################################
# CI operations
################################################################################

headInfo "clean maven build and install of COLA in maven local repository:"
logAndRun -s rm -rf "$mvn_local_repository_dir/com/alibaba/demo"
logAndRun -s rm -rf "$mvn_local_repository_dir/com/alibaba/cola"
logAndRun -s rm -rf "$mvn_local_repository_dir/com/alibaba/craftsman"

(
    headInfo "CI: cola-components"

    cd cola-components/
    MVN clean install
)

(
    headInfo "CI: cola-archetypes"

    cd cola-archetypes/
    MVN clean install
)

(
    headInfo "CI: archetype:generate by cola-framework-archetype-service"

    # NOTE: DO NOT declare archetypeVersion var as readonly, its value is supplied by subshell.
    archetypeVersion=$(
        grep '<version>.*</version>' cola-archetypes/cola-archetype-service/pom.xml |
            awk -F'</?version>' 'NR==1 {print $2}'
    )

    readonly demo_dir="cola-archetypes/target/cola-framework-archetype-service-demo"
    mkdir -p "$demo_dir"
    cd "$demo_dir"

    readonly artifactId=demo-service

    MVN archetype:generate \
        -DgroupId=com.alibaba.cola.demo.archetype-service -DartifactId="$artifactId" -Dversion=1.0.0-SNAPSHOT \
        -Dpackage=com.alibaba.cola.demo.service \
        -DarchetypeGroupId=com.alibaba.cola \
        -DarchetypeArtifactId=cola-framework-archetype-service \
        -DarchetypeVersion="$archetypeVersion" \
        -DarchetypeCatalog=local \
        -DinteractiveMode=false

    cd "$artifactId"
    MVN install
)

(
    headInfo "CI: archetype:generate by cola-framework-archetype-web"

    # NOTE: DO NOT declare archetypeVersion var as readonly, its value is supplied by subshell.
    archetypeVersion=$(
        grep '<version>.*</version>' cola-archetypes/cola-archetype-web/pom.xml |
            awk -F'</?version>' 'NR==1 {print $2}'
    )

    readonly demo_dir="cola-archetypes/target/cola-framework-archetype-web-demo"
    mkdir -p "$demo_dir"
    cd "$demo_dir"

    readonly artifactId=demo-web

    MVN archetype:generate \
        -DgroupId=com.alibaba.cola.demo.archetype-web -DartifactId="$artifactId" -Dversion=1.0.0-SNAPSHOT \
        -Dpackage=com.alibaba.cola.demo.web \
        -DarchetypeGroupId=com.alibaba.cola \
        -DarchetypeArtifactId=cola-framework-archetype-web \
        -DarchetypeVersion="$archetypeVersion" \
        -DarchetypeCatalog=local \
        -DinteractiveMode=false

    cd "$artifactId"
    MVN install
)

(
    headInfo "CI: sample/craftsman"

    cd sample/craftsman/
    MVN clean install
)
