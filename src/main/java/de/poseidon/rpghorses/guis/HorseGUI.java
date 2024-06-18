package de.poseidon.rpghorses.guis;

import de.poseidon.rpghorses.PlayerHorse;
import de.poseidon.rpghorses.RPGHorses;
import de.poseidon.rpghorses.api.ItemBuilder;
import de.poseidon.rpghorses.api.ItemButton;
import de.poseidon.rpghorses.api.MenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HorseGUI extends MenuHolder<RPGHorses> {
    public static final ItemStack PLACEHOLDER = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("").build();

    public HorseGUI() {
        super(RPGHorses.getPlugin(), 9, "Pferde Menu");
        List<Integer> placeholder = List.of(0, 2, 4, 6, 8);
        for (int i : placeholder) {
            setButton(i, new ItemButton<HorseGUI>(PLACEHOLDER));
        }
        setButton(1, new ItemButton<HorseGUI>(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name("Spawn Horse").build()) {
            @Override
            public void onClick(HorseGUI holder, InventoryClickEvent event) {
               PlayerHorse horse = new PlayerHorse((Player) event.getWhoClicked());
               horse.spawn();
            }
        });
        setButton(3, new ItemButton<HorseGUI>(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name("Despawn Horse").build()){
            @Override
            public void onClick(HorseGUI holder, InventoryClickEvent event) {
                PlayerHorse horse = new PlayerHorse((Player) event.getWhoClicked());
                horse.despawn();
            }
        });
        setButton(5, new ItemButton<HorseGUI>(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("Respawn Horse").build()){
            @Override
            public void onClick(HorseGUI holder, InventoryClickEvent event) {
                PlayerHorse horse = new PlayerHorse((Player) event.getWhoClicked());
                horse.respawn();
            }
        });
        setButton(7, new ItemButton<HorseGUI>(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).name("Upgrades").build()){
            @Override
            public void onClick(HorseGUI holder, InventoryClickEvent event) {
                event.getWhoClicked().openInventory(new UpgradeGUI((Player) event.getWhoClicked()).getInventory());
            }
        });
    }
}
