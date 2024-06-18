package de.poseidon.rpghorses.api;


import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public class GuiListener implements Listener {

    private static final GuiListener INSTANCE = new GuiListener();

    //Does not contain inventories whose holders are GuiInventoryHolders. See CraftInventoryCreator.
    private final WeakHashMap<Object/*NMS Inventory*/, WeakReference<GuiInventoryHolder<?>>> guiInventories = new WeakHashMap<>();

    private GuiListener() {}


    public static GuiListener getInstance() {
        return INSTANCE;
    }

    public boolean registerGui(GuiInventoryHolder<?> holder, Inventory inventory) {
        if (holder == inventory.getHolder()) return true; //yes, reference equality

        return guiInventories.putIfAbsent(getBaseInventory(inventory), new WeakReference<>(holder)) == null;
    }

    public GuiInventoryHolder<?> getHolder(Inventory inventory) {
        if (inventory.getLocation() != null) return null;

        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof GuiInventoryHolder) return (GuiInventoryHolder<?>) holder;

        WeakReference<GuiInventoryHolder<?>> reference = guiInventories.get(getBaseInventory(inventory));
        if (reference == null) return null;

        return reference.get();
    }

    public boolean isGuiRegistered(GuiInventoryHolder<?> holder, Inventory inventory) {
        return getHolder(inventory) == holder; //yes, reference equality!
    }

    public boolean isGuiRegistered(Inventory inventory) {
        return getHolder(inventory) != null;
    }

    private void onGuiInventoryEvent(InventoryEvent event, Consumer<GuiInventoryHolder> action) {
        GuiInventoryHolder<?> guiHolder = getHolder(event.getInventory());

        if (guiHolder != null && guiHolder.getPlugin().isEnabled()) {
            action.accept(guiHolder);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        onGuiInventoryEvent(event, gui -> gui.onOpen(event));
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        onGuiInventoryEvent(event, gui -> {
            event.setCancelled(true);
            gui.onClick(event);
        });
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        onGuiInventoryEvent(event, gui -> {
            event.setCancelled(true);
            gui.onDrag(event);
        });
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        onGuiInventoryEvent(event, gui -> gui.onClose(event));
    }


    private static final Class<?> CRAFT_INVENTORY;
    private static final Method GET_INVENTORY;
    static {
        Class<?> craftInventoryClass = null;
        Method getInventoryMethod = null;
        Class<?> serverClass = Bukkit.getServer().getClass();
        if ("CraftServer".equals(serverClass.getSimpleName())) {
            String serverPackage = serverClass.getPackageName();
            String className = serverPackage + ".inventory.CraftInventory";
            try {
                craftInventoryClass = Class.forName(className);
                getInventoryMethod = craftInventoryClass.getMethod("getInventory");
            } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            }
        }
        CRAFT_INVENTORY = craftInventoryClass;
        GET_INVENTORY = getInventoryMethod;
    }

    private static Object getBaseInventory(Inventory inventory) {
        if (CRAFT_INVENTORY != null && GET_INVENTORY != null && CRAFT_INVENTORY.isInstance(inventory)) {
            try {
                return GET_INVENTORY.invoke(inventory);
            } catch (InvocationTargetException | IllegalAccessException ignored) {
            }
        }
        return inventory;
    }

}
