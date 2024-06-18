package de.poseidon.rpghorses.data;

import de.poseidon.rpghorses.RPGHorses;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HorseDataType implements PersistentDataType<String, Entity> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<Entity> getComplexType() {
        return Entity.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull Entity complex, @NotNull PersistentDataAdapterContext context) {
        return complex.getUniqueId().toString();
    }

    @Override
    public @NotNull Entity fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        UUID uuid = UUID.fromString(primitive);
        Entity entity = RPGHorses.getPlugin().getServer().getEntity(uuid);
        if(entity == null){
            throw new RuntimeException("No Entity Found");
        }
        return entity;
    }
}
