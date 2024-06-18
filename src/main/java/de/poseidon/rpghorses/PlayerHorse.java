package de.poseidon.rpghorses;

import de.poseidon.rpghorses.api.ItemBuilder;
import de.poseidon.rpghorses.api.YesNoMenu;
import de.poseidon.rpghorses.data.HorseDataType;
import de.poseidon.rpghorses.data.PlayerDataType;
import de.poseidon.rpghorses.data.PreferenceDataType;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.function.Consumer;

public class PlayerHorse {

    public static final NamespacedKey HORSEKEY = new NamespacedKey(RPGHorses.getPlugin(), "Horse");
    public static final NamespacedKey BUYKEY = new NamespacedKey(RPGHorses.getPlugin(), "HasHorse");
    public static final NamespacedKey DEADKEY = new NamespacedKey(RPGHorses.getPlugin(), "Dead");
    public static final NamespacedKey PLAYERKEY = new NamespacedKey(RPGHorses.getPlugin(), "Player");
    public static final NamespacedKey PREFERENCEKEY = new NamespacedKey(RPGHorses.getPlugin(), "Preference");
    public static final NamespacedKey SADDLEKEY = new NamespacedKey(RPGHorses.getPlugin(), "Saddle");

    private final Player player;
    private Entity horse;

    public PlayerHorse(Player player) {
        this.player = player;
        if (player.getPersistentDataContainer().has(HORSEKEY, new HorseDataType())) {
            try {
                horse = player.getPersistentDataContainer().get(HORSEKEY, new HorseDataType());
            } catch (RuntimeException e) {
                player.getPersistentDataContainer().remove(HORSEKEY);
                return;
            }
            return;
        }
    }

    public boolean isInRange(double range) {
        return player.getLocation().distance(horse.getLocation()) <= range;
    }

    public void spawn() {
        if (player.getPersistentDataContainer().has(HORSEKEY, new HorseDataType()) && horse != null && horse.isInWorld() && !horse.isDead()) {
            horse.teleport(player);
            return;
        }
        if (player.getPersistentDataContainer().getOrDefault(BUYKEY, PersistentDataType.BOOLEAN, false) && !player.getPersistentDataContainer().getOrDefault(DEADKEY, PersistentDataType.BOOLEAN, false)) {
            Entity entit = player.getPersistentDataContainer().getOrDefault(PREFERENCEKEY, new PreferenceDataType(), RPGHorses.getPlugin().getStandartHorsePreference()).spawn(player.getLocation(), horse1 -> {
                horse1.getInventory().setSaddle(new ItemBuilder(Material.SADDLE).changeItemMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(SADDLEKEY, PersistentDataType.BOOLEAN, true)).build());
                horse1.setOwner(player);
                horse1.getPersistentDataContainer().set(PLAYERKEY, new PlayerDataType(), player);
                horse1.customName(Component.text(player.getName() + "'s Horse"));
                horse1.setAI(false);
            });
            player.getPersistentDataContainer().set(HORSEKEY, new HorseDataType(), entit);
            return;
        }
        player.openInventory(new YesNoMenu<>(RPGHorses.getPlugin(), "Willst du ein neues Pferd kaufen?", inventoryClickEvent -> {
            boolean success = RPGHorses.getPlugin().getManager().isEnabled();
            double cost = RPGHorses.getPlugin().getManager().getBuyHorseCost();
            if(!success){
                EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer(player, cost);
                success = response.transactionSuccess();
            }
            if (success) {
                inventoryClickEvent.getWhoClicked().getPersistentDataContainer().set(BUYKEY, PersistentDataType.BOOLEAN, true);
                player.getPersistentDataContainer().set(DEADKEY, PersistentDataType.BOOLEAN, false);
                player.getPersistentDataContainer().set(PREFERENCEKEY, new PreferenceDataType(), RPGHorses.getPlugin().getStandartHorsePreference());
                this.spawn();
            } else {
                player.sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst: " + cost));
                inventoryClickEvent.getView().close();
            }
        }, inventoryClickEvent -> inventoryClickEvent.getView().close()).getInventory());
    }

    public void despawn() {
        if (player.getPersistentDataContainer().has(HORSEKEY)) {
            horse.remove();
            player.getPersistentDataContainer().remove(HORSEKEY);
        }
    }

    public void dead() {
        player.getPersistentDataContainer().remove(HORSEKEY);
        player.getPersistentDataContainer().set(DEADKEY, PersistentDataType.BOOLEAN, true);
    }

    public void respawn() {
        if (player.getPersistentDataContainer().getOrDefault(DEADKEY, PersistentDataType.BOOLEAN, false)) {
            player.openInventory(new YesNoMenu<>(RPGHorses.getPlugin(), "Willst du dein Pferd reviven?", inventoryClickEvent -> {
                double cost = RPGHorses.getPlugin().getManager().getReviveHorseCost();
                boolean success = RPGHorses.getPlugin().getManager().isEnabled();
                if(!success){
                    EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer(player, cost);
                    success = response.transactionSuccess();
                }
                if (success) {
                    inventoryClickEvent.getWhoClicked().getPersistentDataContainer().set(DEADKEY, PersistentDataType.BOOLEAN, false);
                    this.spawn();
                } else {
                    player.sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst " + cost));
                    inventoryClickEvent.getView().close();
                }
            }, inventoryClickEvent -> inventoryClickEvent.getView().close()).getInventory());
        }
    }

    public void editPreference(Consumer<HorsePreference> funktion) {
        HorsePreference horsePreference = player.getPersistentDataContainer().getOrDefault(PREFERENCEKEY, new PreferenceDataType(), RPGHorses.getPlugin().getStandartHorsePreference());
        funktion.accept(horsePreference);
        player.getPersistentDataContainer().set(PREFERENCEKEY, new PreferenceDataType(), horsePreference);
        this.despawn();
        this.spawn();
    }

    public HorsePreference getPreference() {
        return player.getPersistentDataContainer().getOrDefault(PREFERENCEKEY, new PreferenceDataType(), RPGHorses.getPlugin().getStandartHorsePreference());
    }

    public static class HorsePreference {

        private Horse.Style style;
        private Horse.Color color;
        private int baseHealth;
        private int baseSpeed;
        private int baseArmor;

        public HorsePreference(Horse.Style style, Horse.Color color, int baseHealth, int baseSpeed, int baseArmor) {
            this.style = style;
            this.color = color;
            this.baseHealth = baseHealth;
            this.baseSpeed = baseSpeed;
            this.baseArmor = baseArmor;
        }

        public Horse.Style getStyle() {
            return style;
        }

        public Horse.Color getColor() {
            return color;
        }

        public int getBaseHealth() {
            return baseHealth;
        }

        public int getBaseSpeed() {
            return baseSpeed;
        }

        public int getBaseArmor() {
            return baseArmor;
        }

        public void setStyle(Horse.Style style) {
            this.style = style;
        }

        public void setColor(Horse.Color color) {
            this.color = color;
        }

        public void setBaseHealth(int baseHealth) {
            this.baseHealth = baseHealth;
        }

        public void setBaseSpeed(int baseSpeed) {
            this.baseSpeed = baseSpeed;
        }

        public void setBaseArmor(int baseArmor) {
            this.baseArmor = baseArmor;
        }

        public Entity spawn(Location location, Consumer<Horse> funktion) {

            return location.getWorld().spawnEntity(location, EntityType.HORSE, CreatureSpawnEvent.SpawnReason.DEFAULT, entity1 -> {
                if (entity1 instanceof Horse horse1) {
                    horse1.setColor(color);
                    horse1.setStyle(style);
                    horse1.registerAttribute(Attribute.GENERIC_ARMOR);
                    Objects.requireNonNull(horse1.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(baseArmor * RPGHorses.getPlugin().getManager().getMultiplierArmor());
                    horse1.registerAttribute(Attribute.GENERIC_MAX_HEALTH);
                    Objects.requireNonNull(horse1.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(baseHealth * RPGHorses.getPlugin().getManager().getMultiplierHealth() + 10);
                    horse1.registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                    Objects.requireNonNull(horse1.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(baseSpeed * RPGHorses.getPlugin().getManager().getMultiplierSpeed() + 1);
                    funktion.accept(horse1);
                }
            });
        }
    }
}
