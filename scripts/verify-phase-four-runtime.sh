#!/usr/bin/env bash
# Exercise the packaged mod in disposable servers, never a developer's saved worlds.
set -euo pipefail
cd "$(dirname "$0")/.."

version="$(sed -n 's/^mod_version=//p' gradle.properties | tr -d '\r')"
main="$(pwd)/build/libs/iron-age-furniture-$version.jar"
test -f "$main"
mkdir -p build/runtime-verification
receipt="$(pwd)/build/runtime-verification"
sha256sum "$main" > "$receipt/main-before.sha256"

java_args=(
  "-Dorg.gradle.java.installations.paths=$JAVA_HOME"
  -Dorg.gradle.java.installations.auto-detect=false
  -Dorg.gradle.java.installations.auto-download=false
)
args=(--no-daemon --stacktrace --max-workers=2 "${java_args[@]}" "$@")
./gradlew phaseFourProbeJar "${args[@]}"
probe="$(pwd)/build/phaseFourTest/iaf-phase-four-probes.jar"
test -f "$probe"

for loader in 26.3.0.77 26.3.0.83; do
  installation="$(mktemp -d "${RUNNER_TEMP:-${TMPDIR:-/tmp}}/iaf-phase4-$loader.XXXXXX")"
  # Test a normal installed server. Switching NeoGradle versions in one checkout
  # can reuse generated Minecraft classes from the other loader version.
  case "$loader" in
    26.3.0.77) installer_sha=c2eac3cc8a8a5393178f28ad2971ff0cf6be29971fc7e7749a561e43f3dee50b ;;
    26.3.0.83) installer_sha=560574da38d0db4fe67bfdf1ccc43239e2c3bb5282a4447ef259100aea4f5ea9 ;;
  esac
  installer="$installation/installer.jar"
  curl --fail --location --retry 3 --output "$installer" \
    "https://maven.neoforged.net/releases/net/neoforged/neoforge/$loader/neoforge-$loader-installer.jar"
  printf '%s  %s\n' "$installer_sha" "$installer" | sha256sum --check
  native_installation="$installation"
  native_installer="$installer"
  server_args=unix_args.txt
  if command -v cygpath >/dev/null; then
    native_installation="$(cygpath -m "$installation")"
    native_installer="$(cygpath -m "$installer")"
    server_args=win_args.txt
  fi
  if ! "$JAVA_HOME/bin/java" -jar "$native_installer" --installServer "$native_installation" \
      > "$receipt/install-$loader.log" 2>&1; then
    tail -n 120 "$receipt/install-$loader.log"
    exit 1
  fi
  mkdir "$installation/mods"
  cp "$main" "$probe" "$installation/mods/"
  printf 'eula=true\n' > "$installation/eula.txt"
  printf '%s\n' \
    'online-mode=false' 'server-ip=127.0.0.1' 'server-port=0' \
    'level-type=minecraft:flat' \
    'generator-settings={"layers":[{"height":1,"block":"minecraft:bedrock"},{"height":2,"block":"minecraft:dirt"},{"height":1,"block":"minecraft:grass_block"}],"biome":"minecraft:plains","lakes":false,"features":false}' \
    'view-distance=2' 'simulation-distance=2' 'spawn-protection=0' \
    'pause-when-empty-seconds=-1' > "$installation/server.properties"
  command=("$JAVA_HOME/bin/java" -Xmx3G -XX:ActiveProcessorCount=4
    -Dio.netty.transport.noNative=true "@libraries/net/neoforged/neoforge/$loader/$server_args" --nogui)
  if [[ "$(uname -s)" == Linux ]]; then command=(timeout 12m "${command[@]}"); fi
  log="$receipt/neoforge-$loader.log"
  if ! (cd "$installation" && "${command[@]}") > "$log" 2>&1; then
    tail -n 120 "$log"
    exit 1
  fi
  if ! grep -q 'IAF_PHASE4_PROBE_PASS checks=' "$log" ||
      grep -Eq 'IAF_PHASE4_PROBE_FAIL|AssertionError|Encountered an unexpected exception' "$log"; then
    tail -n 120 "$log"
    exit 1
  fi
  grep -E 'IAF_.*_PASS' "$log"
  sha256sum --check "$receipt/main-before.sha256"
done
