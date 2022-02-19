#!/bin/bash
set -eEuo pipefail
cd "$(dirname "$(readlink -f "$0")")"

source bash-buddy/lib/trap_error_info.sh
source bash-buddy/lib/common_utils.sh

################################################################################
# prepare
################################################################################

# shellcheck disable=SC2034
PREPARE_JDKS_INSTALL_BY_SDKMAN=(
    8.322.06.2-amzn
    11.0.14-ms
    17.0.2.8.1-amzn
)

source bash-buddy/lib/prepare_jdks.sh

source bash-buddy/lib/java_build_utils.sh

# here use `install` and `-D performRelease` intended
#   to check release operations.
#
# De-activate a maven profile from command line
#   https://stackoverflow.com/questions/25201430
#
# shellcheck disable=SC2034
JVB_MVN_OPTS=(
    "${JVB_DEFAULT_MVN_OPTS[@]}"
    -DperformRelease -P'!gen-sign'
)

################################################################################
# ci build logic
################################################################################

cd ..

extractFirstElementValueFromPom() {
    (($# == 2)) || die "${FUNCNAME[0]} need only 2 arguments, actual arguments: $*"

    local element=$1
    local pom_file=$2
    grep \<"$element"'>.*</'"$element"\> "$pom_file" | awk -F'</?'"$element"\> 'NR==1 {print $2}'
}

test_cola_archetype() {
    local bkp_mvn_opts=("${JVB_MVN_OPTS[@]}")
    JVB_MVN_OPTS=("${JVB_DEFAULT_MVN_OPTS[@]}")

    (
        cu::head_line_echo "test archetype:generate by cola-framework-archetype-service"

        # NOTE: DO NOT declare archetypeVersion var as readonly, its value is supplied by subshell.
        archetypeVersion=$(extractFirstElementValueFromPom version cola-archetypes/cola-archetype-service/pom.xml)

        # shellcheck disable=SC2030
        readonly demo_dir="cola-archetypes/target/cola-framework-archetype-service-demo"
        rm -rf "$demo_dir"
        mkdir -p "$demo_dir"
        cd "$demo_dir"

        # shellcheck disable=SC2030
        readonly artifactId=demo-service

        jvb::mvn_cmd archetype:generate \
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
        jvb::mvn_cmd install
    )

    (
        cu::head_line_echo "test archetype:generate by cola-framework-archetype-web"

        # NOTE: DO NOT declare archetypeVersion var as readonly, its value is supplied by subshell.
        archetypeVersion=$(extractFirstElementValueFromPom version cola-archetypes/cola-archetype-web/pom.xml)

        # shellcheck disable=SC2031
        readonly demo_dir="cola-archetypes/target/cola-framework-archetype-web-demo"
        rm -rf "$demo_dir"
        mkdir -p "$demo_dir"
        cd "$demo_dir"

        # shellcheck disable=SC2031
        readonly artifactId=demo-web

        jvb::mvn_cmd archetype:generate \
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
        jvb::mvn_cmd install
    )

    JVB_MVN_OPTS=("${bkp_mvn_opts[@]}")
}

########################################
# default jdk 11, do build and test
########################################

export CI_TEST_MODE=true
export DCM_AGENT_SUPRESS_EXCEPTION_STACK=true

default_build_jdk_version=11

prepare_jdks::switch_java_home_to_jdk "$default_build_jdk_version"

cu::head_line_echo "build and test with Java: $JAVA_HOME"

jvb::mvn_cmd clean install

test_cola_archetype

########################################
# test multi-version java
# shellcheck disable=SC2154
########################################
for jhome_var_name in "${JDK_HOME_VAR_NAMES[@]}"; do
    # already tested by above `mvn install`
    [ "JDK${default_build_jdk_version}_HOME" = "$jhome_var_name" ] && continue

    prepare_jdks::switch_java_home_to_jdk "${!jhome_var_name}"

    cu::head_line_echo "test with Java: $JAVA_HOME"

    # just test without build
    jvb::mvn_cmd surefire:test

    test_cola_archetype
done
