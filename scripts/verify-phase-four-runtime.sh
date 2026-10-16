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
  mkdir "$installation/mods"
  cp "$main" "$probe" "$installation/mods/"
  printf 'eula=true\n' > "$installation/eula.txt"
  printf '%s\n' \
    'online-mode=false' 'server-ip=127.0.0.1' 'server-port=0' \
    'level-type=minecraft:flat' \
    'generator-settings={"layers":[{"height":1,"block":"minecraft:bedrock"},{"height":2,"block":"minecraft:dirt"},{"height":1,"block":"minecraft:grass_block"}],"biome":"minecraft:plains","lakes":false,"features":false}' \
    'view-distance=2' 'simulation-distance=2' 'spawn-protection=0' \
    'pause-when-empty-seconds=-1' > "$installation/server.properties"
  # Gradle on Windows needs the native path rather than Git Bash's /tmp alias.
  native_installation="$installation"
  if command -v cygpath >/dev/null; then native_installation="$(cygpath -m "$installation")"; fi
  command=(./gradlew runServer -PpackagedRuntime=true -PphaseFourProbe=true
    "-PrunDirectory=$native_installation" "-Pneo_version=$loader" "${args[@]}")
  if [[ "$(uname -s)" == Linux ]]; then command=(timeout 12m "${command[@]}"); fi
  log="$receipt/neoforge-$loader.log"
  if ! "${command[@]}" > "$log" 2>&1; then tail -n 120 "$log"; exit 1; fi
  if ! grep -q 'IAF_PHASE4_PROBE_PASS checks=' "$log" ||
      grep -Eq 'IAF_PHASE4_PROBE_FAIL|AssertionError|Encountered an unexpected exception' "$log"; then
    tail -n 120 "$log"
    exit 1
  fi
  grep -E 'IAF_.*_PASS' "$log"
  sha256sum --check "$receipt/main-before.sha256"
done
