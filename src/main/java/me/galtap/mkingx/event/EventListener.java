package me.galtap.mkingx.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class EventListener implements Listener {
    private final GuiManager guiManager;

    public EventListener(GuiManager guiManager){

        this.guiManager = guiManager;
    }

    @EventHandler
    public void close(InventoryCloseEvent event){
        guiManager.onClose(event);
    }

    @EventHandler
    public void click(InventoryClickEvent event){
        guiManager.onClick(event);
    }
}
