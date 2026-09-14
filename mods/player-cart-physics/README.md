# Player Cart Physics

A server-side Fabric mod that gives your minecart experimental
physics while you ride it. Name a regular cart `#fast` in an anvil to give it
the same physics when carrying a mob. Empty carts and unnamed mob carts keep
vanilla mechanics.

```mcfunction
/gamerule minecraft:max_minecart_speed 24
```

That sets the limit to 24 blocks per second; the default is 8.

Build and run the tests with Java 25: `./gradlew build`.
