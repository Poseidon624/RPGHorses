package de.poseidon.rpghorses.commands;

import de.poseidon.rpghorses.guis.HorseGUI;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HorseCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return true;
        }
        if (args.length == 0) {
            player.openInventory(new HorseGUI().getInventory());
            return true;
        }
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (NamespacedKey key : player.getPersistentDataContainer().getKeys()) {
                    player.getPersistentDataContainer().remove(key);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
