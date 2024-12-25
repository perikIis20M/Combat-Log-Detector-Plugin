# Combat Log Detector Plugin

The Combat Log Detector is a Minecraft plugin designed to track and detect combat logging (players leaving during PvP). The plugin provides useful notifications via Discord without punishing or killing players in-game, giving server admins the ability to monitor combat logs effectively.

## Features
- **Combat Detection**: Tracks player hits and keeps players tagged as "in combat" for 10 seconds.
- **Combat Log Detection**: Monitors players quitting the server during combat and logs the event.
- **Discord Integration**: Sends real-time notifications to Discord when a combat log occurs, including:
  - Player name
  - World name
  - Coordinates (X, Y, Z)

## Installation

1. Download the plugin JAR file.
2. Place the JAR file in your Minecraft server's `plugins` folder.
3. Start the server to generate the `config.yml` file.
4. Open the `config.yml` file and add your Discord webhook URL.
5. Restart the server to apply the configuration.

## Configuration

The plugin uses a `config.yml` file to configure the Discord webhook URL. Make sure to add your webhook URL to the file after installation. Example:

```yaml
discord-webhook-url: "YOUR_DISCORD_WEBHOOK_URL"
# Combat-Log-Detector-Plugin
