package de.poseidon.rpghorses;

import de.poseidon.rpghorses.api.GuiListener;
import de.poseidon.rpghorses.commands.HorseCommand;
import de.poseidon.rpghorses.listeners.HorseListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Horse;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPGHorses extends JavaPlugin {

    private static RPGHorses plugin;

    public static RPGHorses getPlugin() {
        return plugin;
    }
    private PlayerHorse.HorsePreference standartHorsePreference;

    public PlayerHorse.HorsePreference getStandartHorsePreference() {
        return standartHorsePreference;
    }
    private DataManager manager;

    public DataManager getManager() {
        return manager;
    }

    private Economy economy;

    public Economy getEconomy() {
        return economy;
    }


    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(GuiListener.getInstance(), this);
        getServer().getPluginManager().registerEvents(new HorseListener(), this);
        getCommand("horse").setExecutor(new HorseCommand());
        getCommand("horse").setTabCompleter(new HorseCommand());
        setUpConfig();
        Horse.Style style;
        try {
            style = Horse.Style.valueOf(getConfig().getString("standard.horse.style"));
        } catch (IllegalArgumentException e) {
            style = Horse.Style.NONE;
        }
        Horse.Color color;
        try {
            color = Horse.Color.valueOf(getConfig().getString("standard.horse.color"));

        } catch (IllegalArgumentException e) {
            color = Horse.Color.BROWN;
        }
        int health = getConfig().getInt("standard.horse.health");
        int speed = getConfig().getInt("standard.horse.speed");
        int armor = getConfig().getInt("standard.horse.armor");
        standartHorsePreference = new PlayerHorse.HorsePreference(style, color, health, speed, armor);

        if (!setupEconomy() && getConfig().getBoolean("costs.enable")) {
            getLogger().severe("Vault not found or economy/permissions plugin not hooked!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        manager = new DataManager(getConfig());

    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setUpConfig(){
        FileConfiguration config = getConfig();
        config.addDefault("standard.horse.color", "BROWN");
        config.addDefault("standard.horse.style", "NONE");
        config.addDefault("standard.horse.health", 10);
        config.addDefault("standard.horse.speed", 1);
        config.addDefault("standard.horse.armor", 0);
        config.addDefault("costs.buy", 1000);
        config.addDefault("costs.revive", 1500);
        config.addDefault("costs.change.color", 100);
        config.addDefault("costs.change.style", 100);
        config.addDefault("costs.upgrade.health", 500);
        config.addDefault("costs.upgrade.speed", 500);
        config.addDefault("costs.upgrade.armor", 500);
        config.addDefault("costs.enable", false);
        config.addDefault("max.level.health", 40);
        config.addDefault("max.level.speed", 40);
        config.addDefault("max.level.armor", 40);
        config.addDefault("multiplier.health", 1);
        config.addDefault("multiplier.speed", 0.2);
        config.addDefault("multiplier.armor", 0.5);
        config.options().copyDefaults(true);
        saveConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }


}
