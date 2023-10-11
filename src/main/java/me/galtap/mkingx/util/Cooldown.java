package me.galtap.mkingx.util;

import java.util.concurrent.ConcurrentHashMap;

public final class Cooldown {
    private static ConcurrentHashMap<String, Long> cooldownMap;
    private static long delay;
    private Cooldown(){}

    public static void create() {
        cooldownMap = new ConcurrentHashMap<>();
    }

    public static void setCooldown(String cmd, int second) {
        cooldownMap.put(cmd, System.currentTimeMillis() + second * 1000L);
    }

    public static boolean isHasCooldown(String cmd) {
        if (cooldownMap.containsKey(cmd) && cooldownMap.get(cmd) > System.currentTimeMillis()) {
            delay = (cooldownMap.get(cmd) - System.currentTimeMillis()) / 1000L;
            return true;
        }
        return false;
    }
    public static void reset(String cmd){
        cooldownMap.computeIfPresent(cmd, (key, value) -> System.currentTimeMillis() - 1);
    }

    public static long getDelay() {
        return delay;
    }

}
