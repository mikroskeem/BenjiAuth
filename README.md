# BenjiAuth

An authentication plugin for Waterfall

Its sole purpose is to authenticate players and be configurable as much as possible (mikroskeem™ quality)

## Why another one?
- [BungeeAuth](https://github.com/vik1395/BungeeAuth-Minecraft) is broken as shit and not as flexible as it should be
    * Messages cannot be configured properly, e.g chat colors derp up on some cases
    * Didn't seem to work properly with [FastLogin](https://github.com/games647/FastLogin)
    * It included SQLite library, which was pretty useless (in my case)
    * Didn't have built in GeoIP country support
- Lightweightness and fluent API is important
- Other plugins didn't support BCrypt
- It is a good challenge for me

## Building
`./gradlew build`

## License
Proprietary. Might be relicensed later if it isn't used anymore.