package me.galtap.mkingx.core;

import me.clip.placeholderapi.PlaceholderAPI;
import me.galtap.mkingx.event.GuiHandler;
import me.galtap.mkingx.event.GuiManager;
import me.galtap.mkingx.loader.DataLoader;
import me.galtap.mkingx.model.Winner;
import me.galtap.mkingx.settings.ConfigSettings;
import me.galtap.mkingx.util.LoggerManager;
import me.galtap.mkingx.util.SimpleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MainGUI implements GuiHandler {
    private  static final int INVENTORY_SIZE = 36;
    private final ConfigSettings settings;
    private final DataLoader loader;
    private final List<Integer> voidSlots = List.of(35,34,32,30,28,27);
    private final ItemStack closeInventoryItem;
    private final ItemStack backPageItem;
    private final ItemStack nextPageItem;
    private final GuiManager guiManager;
    private final Map<Integer, Inventory> inventoryPages = new HashMap<>();

    public MainGUI(ConfigSettings settings, DataLoader loader,GuiManager guiManager){

        this.settings = settings;
        this.loader = loader;
        closeInventoryItem = SimpleUtil.createItem(Material.BARRIER,1,settings.getCloseName(),null,null,null);
        backPageItem = SimpleUtil.createItem(Material.ARROW,1,settings.getBackPageName(),null,null,null);
        nextPageItem = SimpleUtil.createItem(Material.ARROW,1,settings.getNextPageName(),null,null,null);
        this.guiManager = guiManager;
    }

    public void open(Player player){
        loadInventories(player);
        player.openInventory(inventoryPages.get(1));
    }

    private void loadInventories(Player player){
        inventoryPages.clear();
        List<Winner> winners = loader.getAllWinners();
        int winnersSize = winners.size();
        if(winnersSize < INVENTORY_SIZE){
            // Создаем первый инвентарь
            Inventory firstInventory = Bukkit.createInventory(player,INVENTORY_SIZE,settings.getInventoryDisplayName());
            loadButtons(firstInventory,winnersSize);
            int top = 1;
            for(Winner winner: winners){
                List<String> fillingLore = fillLore(winner);
                if(fillingLore.isEmpty()) continue;
                ItemStack itemStack = SimpleUtil.winnerToHead(winner,fillingLore, SimpleUtil.replace(settings.getItemName(),String.valueOf(top),"%winner_top%"));
                if(itemStack == null) continue;
                for(int slot = 0;slot< INVENTORY_SIZE;slot++){
                    ItemStack stack = firstInventory.getItem(slot);
                    if(stack != null || voidSlots.contains(slot)) continue;
                    firstInventory.setItem(slot,itemStack);
                    top++;
                    break;
                }
            }
            inventoryPages.put(1,firstInventory);
            guiManager.register(firstInventory,this);
            return;
        }

        // Создаем многостраничные инвентари
        int numPages = winnersSize / INVENTORY_SIZE + (winnersSize % INVENTORY_SIZE != 0 ? 1 : 0);
        for (int page = 1; page <= numPages; page++) {
            Inventory inventory = Bukkit.createInventory(player, INVENTORY_SIZE, settings.getInventoryDisplayName());
            loadButtons(inventory, winnersSize);

            for (int i = (page - 1) * INVENTORY_SIZE; i < page * INVENTORY_SIZE; i++) {
                if (i >= winnersSize) break;

                Winner winner = winners.get(i);
                List<String> fillingLore = fillLore(winner);
                if (fillingLore.isEmpty()) continue;

                ItemStack itemStack = SimpleUtil.winnerToHead(winner, fillingLore, String.valueOf(i+1));
                if (itemStack == null) continue;
                int slot = i - (page - 1) * INVENTORY_SIZE;
                if(inventory.getItem(slot) != null || voidSlots.contains(slot)) continue;
                inventory.setItem(slot, itemStack);
            }
            inventoryPages.put(page, inventory);
            guiManager.register(inventory, this);
        }
    }

    private void loadButtons(Inventory inventory, int winnersSize){
        int maxSize = inventory.getSize();
        int closeInventorySlot = 31;
        inventory.setItem(closeInventorySlot,closeInventoryItem);
        if(winnersSize >= maxSize){
            int backPageSlot = 29;
            inventory.setItem(backPageSlot,backPageItem);
            int nextPageSlot = 33;
            inventory.setItem(nextPageSlot,nextPageItem);
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

    }
    private List<String> fillLore(Winner winner){
        List<String> updateLore = new ArrayList<>();
        try {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(winner.getUuid()));
            for(String text: settings.getItemLore()){
                text = PlaceholderAPI.setPlaceholders(offlinePlayer,text);
                updateLore.add(text);
            }
        }catch (IllegalArgumentException e){
            LoggerManager.UUID_NULL.logFatalError(e);
        }
        return updateLore;
    }
}
