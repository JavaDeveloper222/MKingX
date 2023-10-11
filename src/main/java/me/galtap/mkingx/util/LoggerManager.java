package me.galtap.mkingx.util;

import me.galtap.mkingx.MKingX;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public enum LoggerManager {
    CONNECT_PROCESS("Подключение к базе данных..."),
    CONNECTION_FAILED("Не удалось подключиться к базе данных"),
    CONNECTED( "успешное подключение к базе данных"),
    CLOSE_CONNECTION_ERROR("Ошибка при отключении соединения из базы данных"),
    DERICTORY_CREATE_ERROR("Не удалось создать папку  для файла %s"),
    FILE_CREATE_ERROR("Не удалось создать файл %s"),
    FILE_SAVE_ERROR("Не удалось сохранить файл %s"),
    FILE_RELOAD_ERROR("Не удалось перезагрузить файл %s"),
    MYSQL_TABLE_CREATE_ERROR("Не удалось создать таблицу в базе данных"),
    MYSQL_GET_PLAYER_DATA_ERROR("Не удалось получить данные игроков из базы данных"),
    MYSQL_DATA_UPDATE_ERROR("Не удалось обновить данные игрока в базе данных"),
    VAULT_ERROR("Произошла ошибка при попытке взаимодействовать с плагином Vault"),
    VAULT_LOAD("Возникла ошибка плагина. На сервере не хватает Vault или плагина на экономику"),
    PLACEHOLDER_LOAD("Для полноценной работы плагина нужно скачать PlaceholderAPI"),
    LOCATION_NULL("Не получилось начать игру. Укажите локацию для игры - /king set"),
    UUID_NULL("Возникла ошибка при получении uuid офлайн игрока");

    private static final Logger LOGGER = MKingX.getInstance().getLogger();
    private final String message;
    LoggerManager(String message){
        this.message = message;
    }

    public void logWarning(){
        String text = String.format(message);
        LOGGER.warning(text);
    }
    public void logColorMessage(ChatColor color){
        String text = color+String.format(message);
        LOGGER.info(text);
    }
    public void logFatalError(Throwable e, Object... args){
        String text = String.format(message,args);
        LOGGER.log(Level.WARNING,text,e);
    }
    public void logJustError(Object... args){
        String text = String.format(message,args);
        LOGGER.severe(text);
    }
    public void sendPlayerMessage(Player player,Object... args){
        if(player == null) return;
        String text = String.format(message,args);
        player.sendMessage(text);
    }
}
