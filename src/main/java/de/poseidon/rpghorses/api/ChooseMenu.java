package de.poseidon.rpghorses.api;

import de.poseidon.rpghorses.RPGHorses;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.function.Consumer;

public class ChooseMenu extends MenuHolder<RPGHorses>{
    public ChooseMenu(List<String> choices, Consumer<String> choiceUse) {
        super(RPGHorses.getPlugin(), 9, "Wähle");
        int i = 0;
        for(String s : choices){
            setButton(i, new ItemButton<ChooseMenu>(new ItemBuilder(Material.NOTE_BLOCK).name(s).customModelData(i + 1).build()){
                @Override
                public void onClick(ChooseMenu holder, InventoryClickEvent event) {
                    choiceUse.accept(s);
                    event.getView().close();
                }
            });
            i++;
        }
    }
}
