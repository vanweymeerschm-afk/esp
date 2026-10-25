# MarthoxGuard 1.1.0 — Paper 26.2 / Java 25

## What this does
- Basic server-side block reach checks for block breaking and interactions.
- Staff alerts and optional cancellation for actions beyond a configurable distance.
- Includes a separate Paper Anti-Xray configuration snippet.

## Honest limitations
- **Freecam:** The server cannot directly inspect a player's camera position. Reach checks may catch some attempts to interact with distant blocks, but they are not a universal detector.
- **ESP:** Most ESP is rendered entirely by the player's client. The server cannot reliably detect it. This plugin does not claim to detect all ESP.
- **Anti-Xray:** Use Paper's built-in block obfuscation, configured in `paper-world-defaults-antixray.yml`. Merge the section into `config/paper-world-defaults.yml`, adapting block names to your worlds/version.
- The reach threshold is deliberately simple and may cause false positives due to game mechanics, latency, or custom plugins. Test it before public deployment. Don't auto-ban based on a single alert.

## Build (JDK 25 + Maven)
From this folder run:
```sh
mvn package
```
Output: `target/MarthoxGuard-1.1.0.jar`

The Paper dependency uses Maven version range `[26.2.build,)` based on Paper's current version scheme. A build needs internet access to resolve Paper API and Maven dependencies.

## Install
1. Back up your server.
2. Run `mvn package` using JDK 25.
3. Copy `target/MarthoxGuard-1.1.0.jar` to the server's `plugins` folder.
4. Merge the Anti-Xray snippet into Paper's world defaults config.
5. Fully restart the server (do not use `/reload`).
6. Test with staff online and tune `plugins/MarthoxGuard/config.yml`.

Official Anti-Xray guide: https://docs.papermc.io/paper/anti-xray/
