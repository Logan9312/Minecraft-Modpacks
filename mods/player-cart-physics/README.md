# Player Cart Physics

A server-side Fabric mod for Minecraft 26.2 that gives your minecart experimental
physics while you ride it. Hop out and it switches back to normal.

Keep Minecart Improvements off in the world settings so farm carts keep their
usual physics. Player carts use Minecraft's own acceleration and braking, with
a speed limit controlled by the vanilla gamerule:

```mcfunction
/gamerule minecraft:max_minecart_speed 24
```

That sets the limit to 24 blocks per second; the default is 8.

Build and run the tests with Java 25: `./gradlew build`.
