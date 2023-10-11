package me.galtap.mkingx.util;

import me.galtap.mkingx.model.Winner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permissible;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class SimpleUtil {
    private SimpleUtil(){}

    public static String getColorText(String text) {
        if (text == null){
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static List<String> getColorText(Collection<String> list) {
        return list.stream().map(SimpleUtil::getColorText).collect(Collectors.toList());
    }

    public static double rndInt(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max + 1.0);
    }
    public static boolean isHavePermission(String text, Permissible player) {
        if (player == null || text == null) {
            return false;
        }
        return player.getEffectivePermissions().stream().anyMatch(info -> info.getPermission().startsWith(text));
    }

    public static ItemStack createItem(Material material, int amount, String displayName, List<String> lore, Map<Enchantment, Integer> enchantments, Integer customModelData) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (displayName != null) {
                meta.setDisplayName(displayName);
            }
            if (lore != null) {
                meta.setLore(lore);
            }
            if (customModelData != null && customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }
            itemStack.setItemMeta(meta);
        }
        if (enchantments != null) {
            enchantments.forEach(itemStack::addUnsafeEnchantment);
        }
        return itemStack;
    }
    public static BoundingBox createCubZone(Location center, int radius) {
        double minX = center.getX() - radius;
        double minZ = center.getZ() - radius;
        double maxX = center.getX() + radius;
        double maxZ = center.getZ() + radius;
        double minY = center.getY() - radius;
        double maxY = center.getY() + radius;
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    public static Player getPlayersInCenter(Location center, Iterable<Player> playersInZone) {
        List<Player> playersInCenter = new ArrayList<>();
        for (Player player : playersInZone) {
            if (player.getLocation().distance(center) <= 1.0) {
                playersInCenter.add(player);
            }
        }
        if(playersInCenter.isEmpty()){
            return null;
        }
        return playersInCenter.get(0);
    }
    public static List<Player> getPlayersInZone(BoundingBox boundingBox, Location center){
        return Objects.requireNonNull(center.getWorld()).getNearbyEntities(boundingBox, Player.class::isInstance).stream().map(Player.class::cast).collect(Collectors.toList());
    }
    public static String replace(String text, String value, String placeholder){
        if(text == null){
            return null;
        }
        if(value == null || placeholder == null){
            return text;
        }
        return text.replace(placeholder,value);
    }
    public static void throwPlayer(Entity player, double strength) {
        org.bukkit.util.Vector direction = player.getLocation().getDirection();
        Vector velocity = direction.multiply(strength);
        player.setVelocity(velocity);
    }
    public static ItemStack winnerToHead(Winner winner, List<String> lore, String displayName){
        ItemStack itemStack = createItem(Material.PLAYER_HEAD,1,displayName,lore,null,null);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if(skullMeta == null) return null;
        try {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(winner.getUuid())));
            itemStack.setItemMeta(skullMeta);
        }catch (IllegalArgumentException e){
            return null;
        }
        return itemStack;
    }
}
