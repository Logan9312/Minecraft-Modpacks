# Player Cart Physics (draft)

Minecraft 26.2, dedicated Fabric server. Experimental movement for a regular
minecart while a player rides it; old movement returns on the next tick after
dismounting. No new items, recipes, commands, multipliers or config files.

Keep the world's Minecart Improvements experiment **off** to leave farm carts
on old physics. If the world experiment is on, its global behaviour is preserved.

The existing `/gamerule minecraft:max_minecart_speed 24` sets the player-cart cap
in blocks/second. The mod exposes that rule even with the experiment off; its
default remains 8. Acceleration and braking are Minecraft's own.

Remove Fast Player Minecarts before testing: this mod declares it incompatible
to prevent accidentally combining speed changes. Nothing here installs itself
in Vanilla Ish or changes a server.

## Build and test

Java 25: `./gradlew build runGameTest`. Tests live in a separate source set and
are not shipped in the mod JAR.

The small runtime consists of four mixins: controller selection, collision
context, vanilla-client position packets, and exposing the existing gamerule.
Controller changes preserve the cart entity and passengers; they do not rewrite
saved entities or world feature flags.

Before deployment, test with a real vanilla client: boarding/dismounting on
slopes, corners, activator-rail ejection, portals, reconnecting, and observing
another rider. Server tests don't verify client interpolation or every farm.
Colliding with a player cart can still push another cart.

Source references: [Express Carts](https://github.com/Seercat3160/express-carts)
for selective physics and vanilla-client tracking, and
[Fast Player Minecarts](https://github.com/DeeKahy/fastcarts) for restricting the
change to the exact vanilla passenger-cart class. No custom speed or item code
from either project is included.
