package Periklis20M.pluginDontAskWhyThisIsTheNameNothingSpecial;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginDontAskWhyThisIsTheNameNothingSpecial extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Register the combat log manager
        String webhookUrl = getConfig().getString("discord-webhook-url", "");
        if (webhookUrl.isEmpty()) {
            getLogger().warning("Discord webhook URL is not set in config.yml!");
            return;
        }
        
        getServer().getPluginManager().registerEvents(new CombatLogManager(webhookUrl), this);
        getLogger().info("Combat log system has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
