package de.poseidon.rpghorses.listeners;

import de.poseidon.rpghorses.PlayerHorse;
import de.poseidon.rpghorses.api.ItemBuilder;
import de.poseidon.rpghorses.data.PlayerDataType;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.persistence.PersistentDataType;

import static de.poseidon.rpghorses.PlayerHorse.PLAYERKEY;
import static de.poseidon.rpghorses.PlayerHorse.SADDLEKEY;

public class HorseListener implements Listener {
    @EventHandler
    public void onHorseDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Horse horse){
            if(horse.getPersistentDataContainer().has(PLAYERKEY, new PlayerDataType())){
                try {
                    Player player = horse.getPersistentDataContainer().get(PLAYERKEY, new PlayerDataType());
                    if (player != null) {
                        new PlayerHorse(player).dead();
                        event.getDrops().clear();
                    }
                }
                catch (RuntimeException r){
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onSaddleClick(InventoryClickEvent event){
        if(event.getView().getTopInventory().equals(event.getClickedInventory())){
            if(event.getClickedInventory() instanceof HorseInventory inventory){
                if(inventory.contains(new ItemBuilder(Material.SADDLE).changeItemMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(SADDLEKEY, PersistentDataType.BOOLEAN, true)).build())){
                    event.setCancelled(true);
                }
            }
        }
    }
}
