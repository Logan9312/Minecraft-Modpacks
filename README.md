# Minecraft-Modpacks
A repo for managing the modpacks on my private servers.

# Minecraft pack helper guide

I use this repo to manage Minecraft server modpacks with packwiz, host them through GitHub Pages, and let players update through Prism Launcher.

## Basic idea

Each server has its own packwiz pack.

Players import a Prism instance once. After that, they just relaunch the same instance and it updates automatically.

## Common commands

Run these from inside the specific pack folder.

### Add a Modrinth mod

```bash
packwiz modrinth install fabric-api
```

You can also use a Modrinth URL:

```bash
packwiz modrinth install https://modrinth.com/mod/sodium
```

### Update all mods

```bash
packwiz update --all
packwiz refresh
```

### Refresh after changing configs/files

```bash
packwiz refresh
```

### Commit your changes

```bash
git add .
git commit -m "Update pack"
git push
```

## Normal update flow

```bash
packwiz update --all
packwiz refresh
git add .
git commit -m "Update pack"
git push
```

Then restart the Minecraft server.

Tell players:

```text
Close Minecraft and relaunch the same Prism instance.
Do not re-import the ZIP.
The launcher will update the mods automatically.
```

## Prism pre-launch command

Each Prism instance needs a pre-launch command like this:

```bash
"$INST_JAVA" -jar packwiz-installer-bootstrap.jar https://YOUR_GITHUB_USERNAME.github.io/YOUR_REPO/YOUR_PACK_FOLDER/pack.toml
```

Example:

```bash
"$INST_JAVA" -jar packwiz-installer-bootstrap.jar https://YOUR_GITHUB_USERNAME.github.io/minecraft-packs/smp/pack.toml
```

## Server startup command

Run this before starting the server. That keeps the server on the same pack as the clients.

```bash
java -jar packwiz-installer-bootstrap.jar -g -s server https://YOUR_GITHUB_USERNAME.github.io/YOUR_REPO/YOUR_PACK_FOLDER/pack.toml
```

Then start the server normally:

```bash
java -Xmx8G -jar fabric-server-launch.jar nogui
```

## Server datapacks

Put shared datapack ZIPs in the pack's top-level `datapacks` folder:

```text
vanilla-ish/datapacks/
```

The server pack includes Datapack Injector, so the server can load datapacks from that folder without copying them into `world/datapacks` by hand.

After adding or removing datapack ZIPs:

```bash
cd vanilla-ish
packwiz refresh
git add datapacks index.toml pack.toml
git commit -m "Update datapacks"
git push
```

Then rerun the server packwiz update command in AMP before starting the server:

```bash
java -jar packwiz-installer-bootstrap.jar -g -s server https://modpacks.loganlab.ca/vanilla-ish/pack.toml
```

## Creating a player ZIP

In Prism:

```text
Right-click instance
→ Export
→ Export as ZIP
```

Send the ZIP to players.

They should import it once, then keep using the same instance.

## Player instructions

```text
1. Install Prism Launcher.
2. Add Instance.
3. Import the ZIP.
4. Launch the instance.
5. Let the updater finish.

Important:
Do not re-import the ZIP after updates.
Always use the same instance so JourneyMap, waypoints, keybinds, and local settings stay intact.
```

## Exporting to Modrinth

To make a `.mrpack` for Modrinth:

```bash
packwiz refresh
packwiz modrinth export -o Pack-Name-Version.mrpack
```

Upload the `.mrpack` as a new version on Modrinth.

## Automatic Modrinth publishing

Packs listed in `.github/modrinth-packs.json` are published automatically when their pack folder changes on `main`.

To add another Modrinth project, add one object to `.github/modrinth-packs.json`:

```json
{
  "id": "folder-name",
  "path": "folder-name",
  "name": "Display Name",
  "modrinth_id": "PROJECTID",
  "minecraft_versions": "26.2",
  "loaders": "fabric",
  "version_type": "release"
}
```

The GitHub Actions secret must be named `MODRINTH_TOKEN`.

## Things to remember

* GitHub Pages hosts the pack.
* Prism downloads updates from the `pack.toml` URL.
* The server should use the same `pack.toml` URL as the clients.
* Players should relaunch, not reinstall.
* Avoid syncing personal client files like JourneyMap data, screenshots, `options.txt`, or `servers.dat`.
