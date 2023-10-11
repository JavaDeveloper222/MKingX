package me.galtap.mkingx.event;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {
    private final Map<Inventory, GuiHandler> listeners = new HashMap<>();
    public void register(Inventory inventory, GuiHandler guiHandler){
        listeners.put(inventory,guiHandler);
    }
    public void unregister(Inventory inventory){
        listeners.remove(inventory);
    }
    public void onClick(InventoryClickEvent event){
        GuiHandler handler = listeners.get(event.getClickedInventory());
        if(handler != null){
            handler.onClick(event);
        }
    }
    public void onClose(InventoryEvent event){
       listeners.remove(event.getInventory());
    }
}
