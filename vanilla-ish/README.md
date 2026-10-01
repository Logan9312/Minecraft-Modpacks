# Vanilla Ish — 26.3

The pack used by Gurt. The previous 26.2 pack is preserved in Git history.

Validated on a separate copy of Gurt before deployment. It includes alpha/beta
mods, Etched alpha.8, Experimental Player Carts 0.1.4, and Immersive Paintings
from [PR 157](https://github.com/Luke100000/ImmersivePaintings/pull/157),
commit `7725da3ae5d7fd6d4e2dced1f169467d969e6aa1` (GPL-3.0).
Corresponding source archives are included with the GitHub test release.

FabricExporter keeps the tested 1.0.22 build. Sodium stays on 0.9.2 for the
separately installed experimental Voxy build; Voxy is not distributed here.

Vanilla Tweaks datapacks and crafting tweaks come from
[Vanilla Tweaks](https://vanillatweaks.net/), updated for 26.3.
Nubz' Ancient Flowers retains its original files with a 26.3 compatibility overlay for predicate, item-modifier,
loot-table, and advancement JSON formats. Gameplay behavior still needs testing.
Name Formatting Station is no longer included.

Existing 26.2 worlds need a one-time migration of Color Splash trim and ingredient
material references from `more_colors:` to `colorsplash:` before using this pack.
Gurt is migrated separately; installing the pack does not migrate a world.
