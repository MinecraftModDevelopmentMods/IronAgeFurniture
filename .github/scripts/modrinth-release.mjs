import assert from 'node:assert/strict';
import { createHash, randomUUID } from 'node:crypto';
import { appendFile, mkdir, readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';
import { pathToFileURL } from 'node:url';

const repository = 'MinecraftModDevelopmentMods/IronAgeFurniture';
const project = '9DsXFooq';
const optionalProjects = {
  'biomes-o-plenty': 'HXF82T3G',
  forestry: 'MHni9gjy',
  'immersive-engineering': 'tIm2nV03',
  'oh-the-biomes-youll-go': 'uE1WpIAk',
  'oh-the-biomes-weve-gone': 'NTi7d3Xc',
};

export function latestPhaseFour(releases) {
  const latest = new Map();
  for (const release of releases) {
    if (release.draft || release.prerelease || !/^0\.4\.\d+\.\d+$/.test(release.tag_name)) continue;
    const parts = release.tag_name.split('.').map(Number);
    const previous = latest.get(parts[3]);
    if (!previous || parts[2] > Number(previous.split('.')[2])) latest.set(parts[3], release.tag_name);
  }
  return [...latest.values()].sort((a, b) => Number(a.split('.')[3]) - Number(b.split('.')[3]));
}

export function releaseMetadata(version, properties) {
  assert.match(version, /^\d+\.\d+\.\d+\.\d+$/);
  const values = Object.fromEntries(properties.split(/\r?\n/)
    .filter(line => /^[a-z_]+=/.test(line)).map(line => [line.slice(0, line.indexOf('=')), line.slice(line.indexOf('=') + 1).trim()]));
  assert.equal(values.mod_version, version, 'Tagged mod version differs from the requested release');
  assert.equal(values.mod_archive_name, 'iron-age-furniture');
  assert.ok(['forge', 'neoforge'].includes(values.loader_name), 'Unsupported loader');
  const minecraft = values.minecraft_version.split('.').map(Number);
  assert.ok(minecraft.length >= 2 && minecraft.length <= 3 && minecraft.every(Number.isInteger), 'Invalid Minecraft version');
  const suffix = `${minecraft[0]}${String(minecraft[1]).padStart(2, '0')}${String(minecraft[2] ?? 0).padStart(2, '0')}${values.loader_name === 'forge' ? 1 : 2}`;
  assert.equal(version.split('.')[3], suffix, 'Minecraft version or loader differs from the release suffix');
  const base = `${values.mod_archive_name}-${version}`;
  const optional = (values.curseforge_optionals ?? '').split(',').map(slug => slug.trim()).filter(Boolean);
  return {
    version, minecraft: values.minecraft_version, loader: values.loader_name,
    name: `IronAgeFurniture ${version} for Minecraft ${values.minecraft_version} (${values.loader_name === 'forge' ? 'Forge' : 'NeoForge'})`,
    files: [`${base}.jar`, `${base}-sources.jar`, `${base}-javadoc.jar`],
    dependencies: optional.filter(slug => optionalProjects[slug]).map(slug => `${optionalProjects[slug]}(optional)`),
    externalOptionals: optional.filter(slug => !optionalProjects[slug]),
  };
}

export function checksumFor(manifest, filename) {
  const matches = manifest.split(/\r?\n/).map(line => /^([a-fA-F0-9]{64})\s+\*?(.+)$/.exec(line))
    .filter(match => match?.[2] === filename);
  assert.equal(matches.length, 1, `Expected one published checksum for ${filename}`);
  return matches[0][1].toLowerCase();
}

export function matchingVersion(versions, metadata, hashes) {
  const matches = versions.filter(version => version.version_number === metadata.version);
  assert.ok(matches.length <= 1, 'Multiple Modrinth versions use this release number');
  if (!matches.length) return undefined;
  const existing = matches[0];
  assert.equal(existing.project_id, project, 'Existing Modrinth project differs');
  assert.deepEqual(existing.game_versions, [metadata.minecraft], 'Existing Modrinth Minecraft metadata differs');
  assert.deepEqual(existing.loaders, [metadata.loader], 'Existing Modrinth loader differs');
  assert.equal(existing.version_type, metadata.releaseType ?? 'release', 'Existing Modrinth release channel differs');
  const expectedDependencies = metadata.dependencies.map(value => `${value.split('(')[0]}:optional`).sort();
  const actualDependencies = existing.dependencies.map(value => `${value.project_id}:${value.dependency_type}`).sort();
  assert.deepEqual(actualDependencies, expectedDependencies, 'Existing Modrinth dependencies differ');
  assert.equal(existing.files.length, metadata.files.length, 'Existing Modrinth file set differs');
  for (const filename of metadata.files) {
    const file = existing.files.find(file => file.filename === filename);
    assert.equal(file?.hashes.sha512, hashes[filename].sha512, `Existing Modrinth file differs: ${filename}`);
    assert.equal(file.primary, filename === metadata.files[0], 'Existing primary file differs');
  }
  return existing;
}

async function json(url, { allowNotFound = false } = {}) {
  const headers = { 'User-Agent': 'IronAgeFurniture-release/1.0 (github.com/MinecraftModDevelopmentMods/IronAgeFurniture)' };
  if (url.startsWith('https://api.github.com/')) headers.Authorization = `Bearer ${process.env.GH_TOKEN}`;
  const response = await fetch(url, { headers, signal: AbortSignal.timeout(60000) });
  if (allowNotFound && response.status === 404) return undefined;
  assert.ok(response.ok, `Request failed (${response.status}): ${url}`);
  return response.json();
}

export async function findExistingVersion(metadata, hashes, request = json) {
  const version = await request(`https://api.modrinth.com/v2/project/${project}/version/${encodeURIComponent(metadata.version)}`, { allowNotFound: true });
  return matchingVersion(version ? [version] : [], metadata, hashes);
}

async function output(name, value) {
  if (!process.env.GITHUB_OUTPUT) return;
  const delimiter = randomUUID();
  await appendFile(process.env.GITHUB_OUTPUT, `${name}<<${delimiter}\n${value}\n${delimiter}\n`);
}

async function selectVersions() {
  const requested = process.env.RELEASE_VERSION;
  let versions = [requested];
  if (requested === 'latest-phase4') {
    const releases = [];
    for (let page = 1; ; page++) {
      const batch = await json(`https://api.github.com/repos/${repository}/releases?per_page=100&page=${page}`);
      releases.push(...batch);
      if (batch.length < 100) break;
    }
    versions = latestPhaseFour(releases);
    assert.ok(versions.length, 'No published Phase 4 releases found');
  }
  for (const version of versions) assert.match(version, /^\d+\.\d+\.\d+\.\d+$/);
  await output('versions', JSON.stringify(versions));
  console.log(JSON.stringify(versions));
}

async function prepare(directory) {
  const version = process.env.RELEASE_VERSION;
  assert.match(version, /^\d+\.\d+\.\d+\.\d+$/);
  const source = await json(`https://api.github.com/repos/${repository}/contents/gradle.properties?ref=${version}`);
  const metadata = releaseMetadata(version, Buffer.from(source.content, 'base64').toString('utf8'));
  metadata.releaseType = process.env.RELEASE_TYPE || 'release';
  assert.ok(['release', 'beta', 'alpha'].includes(metadata.releaseType), 'Invalid release channel');
  await mkdir(directory, { recursive: true });
  if (!process.env.RELEASE_ARTIFACT) {
    const release = await json(`https://api.github.com/repos/${repository}/releases/tags/${version}`);
    assert.ok(!release.draft, 'The GitHub release is a draft');
    for (const filename of [...metadata.files, 'SHA256SUMS']) {
      const asset = release.assets.find(asset => asset.name === filename);
      assert.ok(asset, `Published release is missing ${filename}`);
      const response = await fetch(asset.browser_download_url, { signal: AbortSignal.timeout(120000) });
      assert.ok(response.ok, `Published asset download failed: ${filename}`);
      const bytes = Buffer.from(await response.arrayBuffer());
      assert.equal(bytes.length, asset.size, `Published size differs: ${filename}`);
      if (asset.digest) assert.equal(`sha256:${createHash('sha256').update(bytes).digest('hex')}`, asset.digest);
      await writeFile(join(directory, filename), bytes, { flag: 'wx' });
    }
    await writeFile(join(directory, 'CHANGELOG.md'), `Phase 4 for Minecraft ${metadata.minecraft} (${metadata.loader === 'forge' ? 'Forge' : 'NeoForge'}).\n\n[Release notes](${release.html_url})\n`, { flag: 'wx' });
  }
  const manifest = await readFile(join(directory, 'SHA256SUMS'), 'utf8');
  const hashes = {};
  for (const filename of metadata.files) {
    const bytes = await readFile(join(directory, filename));
    const sha256 = createHash('sha256').update(bytes).digest('hex');
    assert.equal(sha256, checksumFor(manifest, filename), `Published SHA256 mismatch: ${filename}`);
    hashes[filename] = { sha256, sha512: createHash('sha512').update(bytes).digest('hex') };
  }
  const existing = await findExistingVersion(metadata, hashes);
  await writeFile(join(directory, 'modrinth-receipt.json'), JSON.stringify({ metadata, hashes, existingVersion: existing?.id }, null, 2));
  for (const [name, value] of Object.entries({ name: metadata.name, minecraft: metadata.minecraft, loader: metadata.loader,
    files: metadata.files.map(file => join(directory, file)).join('\n'), dependencies: metadata.dependencies.join('\n'),
    publish: String(!existing), existing_version: existing?.id ?? '' })) await output(name, value);
  console.log(`${version}: ${existing ? 'identical release already published' : 'verified and ready to publish'}`);
  if (metadata.externalOptionals.length) console.log(`Optional integrations hosted outside Modrinth: ${metadata.externalOptionals.join(', ')}`);
}

async function verify(directory) {
  const receipt = JSON.parse(await readFile(join(directory, 'modrinth-receipt.json'), 'utf8'));
  const id = process.env.MODRINTH_VERSION;
  assert.match(id ?? '', /^[a-zA-Z0-9]+$/, 'Missing Modrinth version ID');
  const published = await json(`https://api.modrinth.com/v2/version/${id}`);
  const version = matchingVersion([published], receipt.metadata, receipt.hashes);
  assert.ok(version, 'Published version is not publicly listed');
  assert.equal(version.status, 'listed');
  for (const file of version.files) {
    const response = await fetch(file.url, { signal: AbortSignal.timeout(120000) });
    assert.ok(response.ok, `Public Modrinth download failed: ${file.filename}`);
    const bytes = Buffer.from(await response.arrayBuffer());
    assert.equal(createHash('sha256').update(bytes).digest('hex'), receipt.hashes[file.filename].sha256);
  }
  receipt.publishedVersion = version.id;
  receipt.publicDownloadsVerified = true;
  receipt.url = `https://modrinth.com/mod/iron-age-furniture/version/${version.id}`;
  await writeFile(join(directory, 'modrinth-receipt.json'), JSON.stringify(receipt, null, 2));
  console.log(`Verified public release ${receipt.url}`);
}

if (process.argv[1] && import.meta.url === pathToFileURL(process.argv[1]).href) {
  assert.equal(process.env.GITHUB_REPOSITORY, repository, 'Publishing is restricted to the MMD repository');
  const command = process.argv[2];
  const directory = join(process.env.RUNNER_TEMP, 'release-input');
  if (command === 'select') await selectVersions();
  else if (command === 'prepare') await prepare(directory);
  else if (command === 'verify') await verify(directory);
  else throw new Error(`Unknown command: ${command}`);
}
