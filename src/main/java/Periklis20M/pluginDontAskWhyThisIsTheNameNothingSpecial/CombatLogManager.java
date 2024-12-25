package Periklis20M.pluginDontAskWhyThisIsTheNameNothingSpecial;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogManager implements Listener {
    private final Map<UUID, Long> playersInCombat = new HashMap<>();
    private final int COMBAT_TIMEOUT = 10; // Combat tag duration in seconds
    private final String webhookUrl;

    public CombatLogManager(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            
            // Tag both players as in combat
            playersInCombat.put(victim.getUniqueId(), System.currentTimeMillis());
            playersInCombat.put(attacker.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInCombat(player)) {
            // Player logged out while in combat
            sendWebhookMessage(player);
            playersInCombat.remove(player.getUniqueId());
        }
    }

    private boolean isInCombat(Player player) {
        if (!playersInCombat.containsKey(player.getUniqueId())) {
            return false;
        }

        long lastCombatTime = playersInCombat.get(player.getUniqueId());
        long timeSinceCombat = (System.currentTimeMillis() - lastCombatTime) / 1000;
        
        if (timeSinceCombat > COMBAT_TIMEOUT) {
            playersInCombat.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    private void sendWebhookMessage(Player player) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonMessage = String.format("{" +
                    "\"content\": null," +
                    "\"embeds\": [{" +
                    "\"title\": \"Combat Log Detected\"," +
                    "\"description\": \"Player %s logged out while in combat!\"," +
                    "\"color\": 16711680," +
                    "\"fields\": [{" +
                    "\"name\": \"Location\"," +
                    "\"value\": \"World: %s\\nX: %d\\nY: %d\\nZ: %d\"" +
                    "}]" +
                    "}]" +
                    "}",
                    player.getName(),
                    player.getWorld().getName(),
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ());

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonMessage.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            connection.getResponseCode();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 