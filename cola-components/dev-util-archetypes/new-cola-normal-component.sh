#!/bin/bash
set -eEuo pipefail
# adjust current dir to script dir
cd "$(dirname "$(readlink -f "$0")")"

source ../../scripts/common.sh
source ../../scripts/common_build.sh

# shellcheck disable=SC2154
[ $# -ne 1 ] && die "need only 1 argument for component name!$nl${nl}usage:$nl  $0 hello"
readonly component_name="$1"
readonly archetype_dir=cola-normal-component-archetype

(
    cd "$archetype_dir"
    MVN_WITH_BASIC_OPTIONS install
)

groupId=$(extractFirstElementValueFromPom groupId ../pom.xml)
component_version=$(extractFirstElementValueFromPom version ../pom.xml)

archetypeGroupId=$(extractFirstElementValueFromPom groupId "$archetype_dir/pom.xml")
archetypeArtifactId=$(extractFirstElementValueFromPom artifactId "$archetype_dir/pom.xml")
archetypeVersion=$(extractFirstElementValueFromPom version "$archetype_dir/pom.xml")

cd ..

MVN_WITH_BASIC_OPTIONS archetype:generate \
    -DgroupId="$groupId" \
    -DartifactId="cola-component-$component_name" \
    -Dversion="$component_version" \
    -Dpackage="com.alibaba.cola.$component_name" \
    -DarchetypeGroupId="$archetypeGroupId" \
    -DarchetypeArtifactId="$archetypeArtifactId" \
    -DarchetypeVersion="$archetypeVersion" \
    -DinteractiveMode=false \
    -DarchetypeCatalog=local
