# Modpack and server change rules

These instructions apply to this entire repository. Read them before changing a
pack, publishing an update, or deploying to a server.

## Mandatory pre-deployment reconciliation

Never assume the GitHub pack is the sole source of truth for the live server.
Administrators may intentionally remove or disable a mod directly on the server.
A packwiz sync can silently reinstall it.

Before adding/updating mods or running any pack sync:

1. Fetch the latest GitHub state without overwriting local changes. Record the
   baseline commit before the requested changes.
2. Obtain a fresh inventory from the actual target server: installed JARs,
   versions, and renamed/disabled JARs. Do not rely on an earlier chat inventory.
3. Compare that inventory against the baseline pack's server-applicable files:
   include `side = "server"`, `side = "both"`, default/both entries, and direct
   JAR files selected for the server by the pack index. Exclude client-only mods.
   Compare mod identity and version, not filenames alone; use `fabric.mod.json`
   or hashes when filenames differ. Bundled dependencies are not standalone gaps.
4. Classify differences as pack-only, server-only, version/hash mismatches, or
   explicitly requested additions/removals. Report unexpected differences.
5. Treat a pre-existing pack mod missing or disabled on the server as a possible
   intentional removal. STOP before any sync or publish that could restore it.
   Ask the user whether to preserve the removal in the pack or reinstall it.
   Do not infer reinstall permission from a request to add an unrelated mod.
6. Do not silently remove server-only mods, overwrite manual version changes,
   or automatically copy every server-only mod into GitHub. Resolve unexpected
   differences with the user. If the server is unreachable, do not claim the
   check passed; defer deployment and publishing changes that could trigger sync.
7. Review the complete proposed sync change set, including dependencies and
   removals. Deploy only the authorized changes. Never run `packwiz update --all`
   as part of a single-mod addition unless explicitly requested.

For Vanilla Ish, Gurt's current host is `192.168.1.217` and its instance directory
is `/home/amp/.ampdata/instances/Gurt01/Minecraft`. Verify the target each time;
do not store SSH passwords, API keys, or license keys in this repository.

## Deployment and verification

- Add existing server mods to the pack at their exact installed version unless
  an upgrade was requested. Verify downloaded hashes and Minecraft/loader support.
- Use appropriate client/server metadata. A server-only mod does not require
  players to install it locally.
- Preserve unrelated working-tree changes and stage only task-owned files.
- Refresh packwiz hashes and inspect the diff before committing. Changes under
  `vanilla-ish` on `main` trigger automatic publishing; publishing is not merely
  a local edit and must respect the reconciliation gate above.
- Unless explicitly told otherwise, deploy/restart only after a fresh console
  query confirms zero online players. If occupied, use the product's scheduled
  follow-up mechanism and recheck immediately before stopping. Never kick players
  or treat AFK players as offline.
- Use a graceful stop and verified AMP controls. Resolve the current process and
  instance before sending console commands; never reuse stale process IDs.
- Preserve manual removals during deployment; use a narrowly scoped installation
  if a broad pack sync would touch unrelated files. This does not waive unresolved
  discrepancies or authorize publishing a pack that would restore removed mods.
- Verify the server returns, the requested mod/version loads, and relevant
  settings/commands work. Check publication results separately from server state.
- Report what changed, any remaining drift, and whether deployment is live or
  pending. Pause completed deployment follow-ups to prevent repeat restarts.

## Scope of this safeguard

`AGENTS.md` guides agents working in this repository; it does not intercept a
human's manual packwiz command, AMP startup hook, or other automated updater.
Do not describe these instructions as a technical lock on those systems.
