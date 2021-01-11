#!/bin/bash
set -eEuo pipefail
# adjust current dir to script dir
cd "$(dirname "$(readlink -f "$0")")"

source common.sh
source common_build.sh

# shellcheck disable=SC2154
[ $# -ne 1 ] && die "need only 1 argument for version!$nl${nl}usage:$nl  $0 4.x.y"
readonly bump_version="$1"

(
    headInfo "bump cola version of cola-components to $bump_version"
    cd ../cola-components/

    MVN_WITH_BASIC_OPTIONS \
        org.codehaus.mojo:versions-maven-plugin:2.8.1:set \
        -DgenerateBackupPoms=false \
        -DnewVersion="$bump_version"
)

(
    headInfo "bump cola version of cola-archetypes to $bump_version"
    cd ../cola-archetypes/

    MVN_WITH_BASIC_OPTIONS \
        org.codehaus.mojo:versions-maven-plugin:2.8.1:set \
        -DgenerateBackupPoms=false \
        -DnewVersion="$bump_version"

    logAndRun -s \
        sed -ri 's~(<cola.components.version>)(.*)(</cola.components.version>)~\1'"$bump_version"'\3~' \
        cola-archetype-service/src/main/resources/archetype-resources/pom.xml \
        cola-archetype-web/src/main/resources/archetype-resources/pom.xml
)
