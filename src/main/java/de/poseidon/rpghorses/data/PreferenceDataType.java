package de.poseidon.rpghorses.data;

import de.poseidon.rpghorses.PlayerHorse;
import org.bukkit.entity.Horse;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PreferenceDataType implements PersistentDataType<String, PlayerHorse.HorsePreference> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<PlayerHorse.HorsePreference> getComplexType() {
        return PlayerHorse.HorsePreference.class;
    }

    @Override
    public @NotNull String toPrimitive(PlayerHorse.@NotNull HorsePreference complex, @NotNull PersistentDataAdapterContext context) {
        return complex.getColor().name() + "|" +complex.getStyle().name() + "|" + complex.getBaseHealth() + "|" + complex.getBaseSpeed() + "|" + complex.getBaseArmor();
    }

    @Override
    public @NotNull PlayerHorse.HorsePreference fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        String[] parts = primitive.split("\\|");
        Horse.Color color = Horse.Color.valueOf(parts[0]);
        Horse.Style style = Horse.Style.valueOf(parts[1]);
        int health = Integer.parseInt(parts[2]);
        int speed = Integer.parseInt(parts[3]);
        int armor = Integer.parseInt(parts[4]);
        return new PlayerHorse.HorsePreference(style, color, health, speed, armor);
    }
}
