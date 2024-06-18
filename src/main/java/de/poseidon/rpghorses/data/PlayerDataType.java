package de.poseidon.rpghorses.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerDataType implements PersistentDataType<String, Player> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Player> getComplexType() {
        return Player.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull Player complex, @NotNull PersistentDataAdapterContext context) {
        return complex.getUniqueId().toString();
    }

    @Override
    public @NotNull Player fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        UUID uuid = UUID.fromString(primitive);
        Player player = Bukkit.getPlayer(uuid);
        if(player == null){
            throw new RuntimeException("No Player Found");
        }
        return player;
    }
}
