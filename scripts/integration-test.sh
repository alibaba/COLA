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

# shellcheck source=common.sh
source "$(dirname "$(readlink -f "$0")")/common.sh"
# shellcheck source=common_build.sh
source "$(dirname "$(readlink -f "$0")")/common_build.sh"

# adjust current dir to project root dir
cd "$(dirname "$(readlink -f "$0")")/.."

################################################################################
# CI operations
################################################################################

cleanMavenInstallOfColaInMavenLocalRepository

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
    archetypeVersion=$(extractFirstElementValueFromPom version cola-archetypes/cola-archetype-service/pom.xml)

    # shellcheck disable=SC2030
    readonly demo_dir="cola-archetypes/target/cola-framework-archetype-service-demo"
    mkdir -p "$demo_dir"
    cd "$demo_dir"

    # shellcheck disable=SC2030
    readonly artifactId=demo-service

    MVN archetype:generate \
        -DgroupId=com.alibaba.cola.demo.archetype-service \
        -DartifactId="$artifactId" \
        -Dversion=1.0.0-SNAPSHOT \
        -Dpackage=com.alibaba.cola.demo.service \
        -DarchetypeGroupId=com.alibaba.cola \
        -DarchetypeArtifactId=cola-framework-archetype-service \
        -DarchetypeVersion="$archetypeVersion" \
        -DinteractiveMode=false \
        -DarchetypeCatalog=local

    cd "$artifactId"
    MVN_WITH_BASIC_OPTIONS install
)

(
    headInfo "CI: archetype:generate by cola-framework-archetype-web"

    # NOTE: DO NOT declare archetypeVersion var as readonly, its value is supplied by subshell.
    archetypeVersion=$(extractFirstElementValueFromPom version cola-archetypes/cola-archetype-web/pom.xml)

    # shellcheck disable=SC2031
    readonly demo_dir="cola-archetypes/target/cola-framework-archetype-web-demo"
    mkdir -p "$demo_dir"
    cd "$demo_dir"

    # shellcheck disable=SC2031
    readonly artifactId=demo-web

    MVN archetype:generate \
        -DgroupId=com.alibaba.cola.demo.archetype-web \
        -DartifactId="$artifactId" \
        -Dversion=1.0.0-SNAPSHOT \
        -Dpackage=com.alibaba.cola.demo.web \
        -DarchetypeGroupId=com.alibaba.cola \
        -DarchetypeArtifactId=cola-framework-archetype-web \
        -DarchetypeVersion="$archetypeVersion" \
        -DinteractiveMode=false \
        -DarchetypeCatalog=local

    cd "$artifactId"
    MVN_WITH_BASIC_OPTIONS install
)

(
    headInfo "CI: samples/craftsman"

    cd samples/craftsman/
    MVN_WITH_BASIC_OPTIONS clean install
)
