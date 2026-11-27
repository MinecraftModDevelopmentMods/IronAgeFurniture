import assert from 'node:assert/strict';
import { test } from 'node:test';
import { checksumFor, findExistingVersion, latestPhaseFour, matchingVersion, releaseMetadata } from './modrinth-release.mjs';

test('backfill selects the newest stable Phase 4 release for each target', () => {
  const tags = ['0.4.1.121012', '0.4.3.121011', '0.3.0.120012', '0.5.0.121011', '0.4.1.121011', '0.4.2.121012'];
  const releases = tags.map(tag_name => ({ tag_name }));
  releases.push({ tag_name: '0.4.9.121011', prerelease: true }, { tag_name: '0.4.8.121011', draft: true });
  assert.deepEqual(latestPhaseFour(releases), ['0.4.3.121011', '0.4.2.121012']);
});

const properties = 'mod_version=0.4.1.121012\nmod_archive_name=iron-age-furniture\nminecraft_version=1.21.1\nloader_name=neoforge\ncurseforge_optionals=biomes-o-plenty,natura';
const metadata = releaseMetadata('0.4.1.121012', properties);

test('tag metadata preserves the target and keeps available integrations optional', () => {
  assert.equal(metadata.minecraft, '1.21.1');
  assert.equal(metadata.loader, 'neoforge');
  assert.deepEqual(metadata.dependencies, ['HXF82T3G(optional)']);
  assert.deepEqual(metadata.externalOptionals, ['natura']);
  assert.throws(() => releaseMetadata('0.4.1.121012', properties.replace('loader_name=neoforge', 'loader_name=forge')));
  assert.throws(() => releaseMetadata('0.4.2.121012', properties));
});

test('release checksums require an exact filename and one manifest entry', () => {
  const hash = 'ab'.repeat(32);
  assert.equal(checksumFor(`${hash}  main.jar\n`, 'main.jar'), hash);
  assert.throws(() => checksumFor(`${hash}  wrong.jar`, 'main.jar'));
  assert.throws(() => checksumFor(`${hash}  main.jar\n${hash}  main.jar`, 'main.jar'));
});

test('retries accept only the exact existing Modrinth file set and target', () => {
  const hashes = Object.fromEntries(metadata.files.map(file => [file, { sha512: 'abc' }]));
  const version = { id: 'existing', project_id: '9DsXFooq', version_number: metadata.version, game_versions: [metadata.minecraft], loaders: [metadata.loader],
    version_type: 'release', dependencies: [{ project_id: 'HXF82T3G', dependency_type: 'optional' }],
    files: metadata.files.map((filename, index) => ({ filename, primary: index === 0, hashes: { sha512: 'abc' } })) };
  assert.equal(matchingVersion([version], metadata, hashes).id, 'existing');
  assert.equal(matchingVersion([], metadata, hashes), undefined);
  assert.throws(() => matchingVersion([version, version], metadata, hashes));
  assert.throws(() => matchingVersion([{ ...version, project_id: 'another' }], metadata, hashes));
  assert.throws(() => matchingVersion([{ ...version, loaders: ['forge'] }], metadata, hashes));
  assert.throws(() => matchingVersion([{ ...version, dependencies: [] }], metadata, hashes));
  assert.throws(() => matchingVersion([{ ...version, version_type: 'alpha' }], metadata, hashes));
  const changed = structuredClone(version);
  changed.files[0].hashes.sha512 = 'different';
  assert.throws(() => matchingVersion([changed], metadata, hashes));
});

test('duplicate checks look up the release directly instead of a stale project listing', async () => {
  const hashes = Object.fromEntries(metadata.files.map(file => [file, { sha512: 'abc' }]));
  const version = { id: 'existing', project_id: '9DsXFooq', version_number: metadata.version, game_versions: [metadata.minecraft], loaders: [metadata.loader],
    version_type: 'release', dependencies: [{ project_id: 'HXF82T3G', dependency_type: 'optional' }],
    files: metadata.files.map((filename, index) => ({ filename, primary: index === 0, hashes: { sha512: 'abc' } })) };
  const request = async (url, options) => {
    assert.equal(url, 'https://api.modrinth.com/v2/project/9DsXFooq/version/0.4.1.121012');
    assert.equal(options.allowNotFound, true);
    return version;
  };
  assert.equal((await findExistingVersion(metadata, hashes, request)).id, 'existing');
  assert.equal(await findExistingVersion(metadata, hashes, async () => undefined), undefined);
  await assert.rejects(findExistingVersion(metadata, hashes, async () => { throw new Error('HTTP 503'); }), /HTTP 503/);
});
