#!/bin/bash
# SDKMAN! with Travis
# https://objectcomputing.com/news/2019/01/07/sdkman-travis

[ -z "${__source_guard_81039D15_3DA9_4EA3_A751_E83DBA84C038:+dummy}" ] || return 0
__source_guard_81039D15_3DA9_4EA3_A751_E83DBA84C038=true

set -eEuo pipefail

# shellcheck source=common.sh
source "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")/common.sh"

__loadSdkman() {
    local this_time_install_sdk_man=false
    # install sdkman
    if [ ! -f "$HOME/.sdkman/bin/sdkman-init.sh" ]; then
        [ -d "$HOME/.sdkman" ] && rm -rf "$HOME/.sdkman"

        curl -s get.sdkman.io | bash || die "fail to install sdkman"
        echo sdkman_auto_answer=true >>"$HOME/.sdkman/etc/config"

        this_time_install_sdk_man=true
    fi

    set +u
    # shellcheck disable=SC1090
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    "$this_time_install_sdk_man" && logAndRun sdk ls java
    set -u
}
__loadSdkman

jdks_install_by_sdkman=(
    8.0.275-amzn

    9.0.7-zulu
    #10.0.2-zulu
    11.0.9-zulu

    #12.0.2-open
    #13.0.5-zulu
    #14.0.2-zulu
    15.0.1-zulu
)
java_home_var_names=()

__setJdkHomeVarsAndInstallJdk() {
    blueEcho "prepared jdks:"

    local jdkNameOfSdkman
    for jdkNameOfSdkman in "${jdks_install_by_sdkman[@]}"; do
        local jdkVersion
        jdkVersion=$(echo "$jdkNameOfSdkman" | awk -F'[.]' '{print $1}')

        # jdkHomeVarName like JDK7_HOME, JDK11_HOME
        local jdkHomeVarName="JDK${jdkVersion}_HOME"

        if [ ! -d "${!jdkHomeVarName:-}" ]; then
            local jdkHomePath="$SDKMAN_CANDIDATES_DIR/java/$jdkNameOfSdkman"

            # set JDKx_HOME to global var java_home_var_names
            eval "$jdkHomeVarName='${jdkHomePath}'"

            # install jdk by sdkman
            [ ! -d "$jdkHomePath" ] && {
                set +u
                logAndRun sdk install java "$jdkNameOfSdkman" || die "fail to install jdk $jdkNameOfSdkman by sdkman"
                set -u
            }
        fi

        java_home_var_names=(${java_home_var_names[@]:+"${java_home_var_names[@]}"} "$jdkHomeVarName")
        printf '%s :\n\t%s\n\tspecified is %s\n' "$jdkHomeVarName" "${!jdkHomeVarName}" "$jdkNameOfSdkman"
    done

    echo
    blueEcho "ls $SDKMAN_CANDIDATES_DIR/java/ :"
    ls -la "$SDKMAN_CANDIDATES_DIR/java/"
}
__setJdkHomeVarsAndInstallJdk
