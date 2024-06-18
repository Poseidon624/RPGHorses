package de.poseidon.rpghorses;

import org.bukkit.configuration.file.FileConfiguration;

public class DataManager {

    private final double buyHorseCost;
    private final double reviveHorseCost;
    private final double changeColorCost;
    private final double changeStylCost;
    private final double upgradeHealthCost;
    private final double upgradeSpeedCost;
    private final double upgradeArmorCost;
    private final double maxLevelHealth;
    private final double maxLevelSpeed;
    private final double maxLevelArmor;
    private final double multiplierHealth;
    private final double multiplierSpeed;
    private final double multiplierArmor;

    public DataManager(FileConfiguration config) {
        buyHorseCost = config.getDouble("costs.buy");
        reviveHorseCost = config.getDouble("costs.revive");
        changeColorCost = config.getDouble("costs.change.color");
        changeStylCost = config.getDouble("costs.change.style");
        upgradeHealthCost = config.getDouble("costs.upgrade.health");
        upgradeSpeedCost = config.getDouble("costs.upgrade.speed");
        upgradeArmorCost = config.getDouble("costs.upgrade.armor");
        maxLevelHealth = config.getDouble("max.level.health");
        maxLevelSpeed = config.getDouble("max.level.speed");
        maxLevelArmor = config.getDouble("max.level.armor");
        multiplierHealth = config.getDouble("multiplier.health");
        multiplierSpeed = config.getDouble("multiplier.speed");
        multiplierArmor = config.getDouble("multiplier.armor");
    }


    public double getBuyHorseCost() {
        return buyHorseCost;
    }

    public double getReviveHorseCost() {
        return reviveHorseCost;
    }

    public double getChangeColorCost() {
        return changeColorCost;
    }

    public double getChangeStylCost() {
        return changeStylCost;
    }

    public double getUpgradeSpeedCost() {
        return upgradeSpeedCost;
    }

    public double getUpgradeHealthCost() {
        return upgradeHealthCost;
    }

    public double getUpgradeArmorCost() {
        return upgradeArmorCost;
    }

    public double getMaxLevelHealth() {
        return maxLevelHealth;
    }

    public double getMaxLevelSpeed() {
        return maxLevelSpeed;
    }

    public double getMaxLevelArmor() {
        return maxLevelArmor;
    }

    public double getMultiplierHealth() {
        return multiplierHealth;
    }

    public double getMultiplierSpeed() {
        return multiplierSpeed;
    }

    public double getMultiplierArmor() {
        return multiplierArmor;
    }
}
