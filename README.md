# Minecraft-Modpacks
A simple repo for managing modpacks on my private servers

# Minecraft Pack Helper Guide

This repo is used to manage Minecraft server modpacks with **packwiz**, host them through **GitHub Pages**, and let players update through **Prism Launcher**.

## Basic Idea

Each server has its own packwiz pack.

Players import a Prism instance once. After that, they just relaunch the same instance and it updates automatically.

## Common Commands

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

### Commit changes

```bash
git add .
git commit -m "Update pack"
git push
```

## Normal Update Flow

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

## Prism Pre-Launch Command

Each Prism instance needs a pre-launch command like this:

```bash
"$INST_JAVA" -jar packwiz-installer-bootstrap.jar https://YOUR_GITHUB_USERNAME.github.io/YOUR_REPO/YOUR_PACK_FOLDER/pack.toml
```

Example:

```bash
"$INST_JAVA" -jar packwiz-installer-bootstrap.jar https://YOUR_GITHUB_USERNAME.github.io/minecraft-packs/smp/pack.toml
```

## Server Startup Command

Run this before starting the server so the server updates from the same pack:

```bash
java -jar packwiz-installer-bootstrap.jar -g -s server https://YOUR_GITHUB_USERNAME.github.io/YOUR_REPO/YOUR_PACK_FOLDER/pack.toml
```

Then start the server normally:

```bash
java -Xmx8G -jar fabric-server-launch.jar nogui
```

## Creating a Player ZIP

In Prism:

```text
Right-click instance
→ Export
→ Export as ZIP
```

Send the ZIP to players.

They should import it once, then keep using the same instance.

## Player Instructions

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

Upload that `.mrpack` as a new version on Modrinth.

## Things to Remember

* GitHub Pages hosts the pack.
* Prism downloads updates from the `pack.toml` URL.
* The server should use the same `pack.toml` URL as the clients.
* Players should relaunch, not reinstall.
* Avoid syncing personal client files like JourneyMap data, screenshots, `options.txt`, or `servers.dat`.
