package de.poseidon.rpghorses.guis;

import de.poseidon.rpghorses.PlayerHorse;
import de.poseidon.rpghorses.RPGHorses;
import de.poseidon.rpghorses.api.ChooseMenu;
import de.poseidon.rpghorses.api.ItemBuilder;
import de.poseidon.rpghorses.api.ItemButton;
import de.poseidon.rpghorses.api.MenuHolder;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;

import static de.poseidon.rpghorses.guis.HorseGUI.PLACEHOLDER;

public class UpgradeGUI extends MenuHolder<RPGHorses> {

    public UpgradeGUI(Player player) {
        super(RPGHorses.getPlugin(), 9, "Upgrade Menu");
        for (int i : List.of(1, 3, 5, 7)) {
            setButton(i, new ItemButton<UpgradeGUI>(PLACEHOLDER));
        }
        PlayerHorse.HorsePreference preference = new PlayerHorse(player).getPreference();
        redoButtons(preference);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        super.onClick(event);
        redoButtons(new PlayerHorse((Player) event.getWhoClicked()).getPreference());

    }

    private void redoButtons(PlayerHorse.HorsePreference preference) {
        setButton(0, new ItemButton<UpgradeGUI>(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name("Buy Health").lore("Level: " + preference.getBaseHealth()).build()) {
            @Override
            public void onClick(UpgradeGUI holder, InventoryClickEvent event) {
                double cost = RPGHorses.getPlugin().getManager().getUpgradeHealthCost();
                boolean success = !RPGHorses.getPlugin().getManager().isEnabled();
                if(!success){
                    EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer((OfflinePlayer) event.getWhoClicked(), cost);
                    success = response.transactionSuccess();
                }
                if (success) {
                    PlayerHorse playerHorse = new PlayerHorse((Player) event.getWhoClicked());
                    playerHorse.editPreference(horsePreference -> {
                        if (horsePreference.getBaseHealth() >= RPGHorses.getPlugin().getManager().getMaxLevelHealth()) {
                            event.getWhoClicked().sendMessage(Component.text("Du hast das Maximale Level in Health erreicht"));
                            return;
                        }
                        horsePreference.setBaseHealth(horsePreference.getBaseHealth() + 1);
                    });
                } else {
                    event.getWhoClicked().sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst: " + cost));
                    event.getView().close();
                }
            }
        });
        setButton(2, new ItemButton<UpgradeGUI>(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).name("Buy Speed").lore("Level: " + preference.getBaseSpeed()).build()) {
            @Override
            public void onClick(UpgradeGUI holder, InventoryClickEvent event) {
                double cost = RPGHorses.getPlugin().getManager().getUpgradeSpeedCost();
                boolean success = !RPGHorses.getPlugin().getManager().isEnabled();
                if(!success){
                    EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer((OfflinePlayer) event.getWhoClicked(), cost);
                    success = response.transactionSuccess();
                }
                if (success) {
                    PlayerHorse playerHorse = new PlayerHorse((Player) event.getWhoClicked());
                    playerHorse.editPreference(horsePreference -> {
                        if (horsePreference.getBaseSpeed() >= RPGHorses.getPlugin().getManager().getMaxLevelSpeed()) {
                            event.getWhoClicked().sendMessage(Component.text("Du hast das Maximale Level in Speed erreicht"));
                            return;
                        }
                        horsePreference.setBaseSpeed(horsePreference.getBaseSpeed() + 1);
                    });
                } else {
                    event.getWhoClicked().sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst: " + cost));
                    event.getView().close();
                }
            }
        });
        setButton(4, new ItemButton<UpgradeGUI>(new ItemBuilder(Material.BROWN_STAINED_GLASS_PANE).name("Buy Armor").lore("Level: " + preference.getBaseArmor()).build()) {
            @Override
            public void onClick(UpgradeGUI holder, InventoryClickEvent event) {
                double cost = RPGHorses.getPlugin().getManager().getUpgradeArmorCost();
                boolean success = !RPGHorses.getPlugin().getManager().isEnabled();
                if(!success){
                    EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer((OfflinePlayer) event.getWhoClicked(), cost);
                    success = response.transactionSuccess();
                }
                if (success) {

                    PlayerHorse playerHorse = new PlayerHorse((Player) event.getWhoClicked());
                    playerHorse.editPreference(horsePreference -> {
                        if (horsePreference.getBaseArmor() >= RPGHorses.getPlugin().getManager().getMaxLevelArmor()) {
                            event.getWhoClicked().sendMessage(Component.text("Du hast das Maximale Armor in Speed erreicht"));
                            return;
                        }
                        horsePreference.setBaseArmor(horsePreference.getBaseArmor() + 1);
                    });
                } else {
                    event.getWhoClicked().sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst: " + cost));
                    event.getView().close();
                }
            }
        });
        setButton(6, new ItemButton<UpgradeGUI>(new ItemBuilder(Material.STICK).name("Change Color").build()) {
            @Override
            public void onClick(UpgradeGUI holder, InventoryClickEvent event) {
                event.getWhoClicked().openInventory(new ChooseMenu(Arrays.stream(Horse.Color.values()).map(Enum::name).toList(), s -> {
                    double cost = RPGHorses.getPlugin().getManager().getChangeColorCost();
                    boolean success = !RPGHorses.getPlugin().getManager().isEnabled();
                    if(!success){
                        EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer((OfflinePlayer) event.getWhoClicked(), cost);
                        success = response.transactionSuccess();
                    }
                    if (success) {
                        Horse.Color color = Horse.Color.valueOf(s);
                        new PlayerHorse((Player) event.getWhoClicked()).editPreference(preference1 -> preference1.setColor(color));
                    } else {
                        event.getWhoClicked().sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst: " + cost));
                        event.getView().close();
                    }
                }).getInventory());
            }
        });

        setButton(8, new ItemButton<UpgradeGUI>(new ItemBuilder(Material.GOLDEN_SHOVEL).name("Change Style").build()) {
            @Override
            public void onClick(UpgradeGUI holder, InventoryClickEvent event) {
                event.getWhoClicked().openInventory(new ChooseMenu(Arrays.stream(Horse.Style.values()).map(Enum::name).toList(), s -> {
                    double cost = RPGHorses.getPlugin().getManager().getChangeStylCost();
                    boolean success = !RPGHorses.getPlugin().getManager().isEnabled();
                    if(!success){
                        EconomyResponse response = RPGHorses.getPlugin().getEconomy().withdrawPlayer((OfflinePlayer) event.getWhoClicked(), cost);
                        success = response.transactionSuccess();
                    }
                    if (success) {
                        Horse.Style style = Horse.Style.valueOf(s);
                        new PlayerHorse((Player) event.getWhoClicked()).editPreference(preference1 -> preference1.setStyle(style));
                    } else {
                        event.getWhoClicked().sendMessage(Component.text("Du hast nicht genug Geld dafür. Du brauchst: " + cost));
                        event.getView().close();
                    }
                }).getInventory());
            }
        });
    }
}
