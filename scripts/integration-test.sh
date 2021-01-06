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

source "$(dirname "$(readlink -f "$BASH_SOURCE")")/common_build.sh"

# adjust current dir to project root dir
cd "$(dirname "$0")/.."

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
    MVN_WITH_BASIC_OPTIONS install
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
    MVN_WITH_BASIC_OPTIONS install
)

(
    headInfo "CI: sample/craftsman"

    cd sample/craftsman/
    MVN_WITH_BASIC_OPTIONS clean install
)
