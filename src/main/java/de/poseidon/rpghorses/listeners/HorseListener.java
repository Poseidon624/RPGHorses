package de.poseidon.rpghorses.listeners;

import de.poseidon.rpghorses.PlayerHorse;
import de.poseidon.rpghorses.data.PlayerDataType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static de.poseidon.rpghorses.PlayerHorse.PLAYERKEY;

public class HorseListener implements Listener {
    @EventHandler
    public void onHorseDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Horse horse){
            if(horse.getPersistentDataContainer().has(PLAYERKEY, new PlayerDataType())){
                try {
                    Player player = horse.getPersistentDataContainer().get(PLAYERKEY, new PlayerDataType());
                    if (player != null) {
                        new PlayerHorse(player).dead();
                    }
                }
                catch (RuntimeException r){
                    return;
                }
            }
        }
    }

}
